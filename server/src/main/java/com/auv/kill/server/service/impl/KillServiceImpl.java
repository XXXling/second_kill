package com.auv.kill.server.service.impl;

import com.auv.kill.model.entity.ItemKill;
import com.auv.kill.model.entity.ItemKillSuccess;
import com.auv.kill.model.mapper.ItemKillDao;
import com.auv.kill.model.mapper.ItemKillSuccessDao;
import com.auv.kill.server.enums.OrderStatus;
import com.auv.kill.server.service.KillService;
import com.auv.kill.server.service.RabbitSenderService;
import com.auv.kill.server.utils.SnowFlake;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/12 16:20
 */
@Service
public class KillServiceImpl implements KillService {
    private static final Logger log = LoggerFactory.getLogger(KillService.class);

    private SnowFlake snowFlake = new SnowFlake(2, 3);

    @Autowired
    private ItemKillSuccessDao itemKillSuccessDao;
    @Autowired
    private ItemKillDao itemKillDao;

    @Autowired
    private RabbitSenderService rabbitSenderService;

    /**
     * 商品秒杀核心业务逻辑的处理
     *
     * @param killId:
     * @param userId:
     * @return: java.lang.Boolean
     */
    @Override
    public Boolean killItem(Integer killId, Integer userId) throws Exception {
        Boolean result = false;
        // 判断用户是否已经抢购过商品，已抢购则返回false
        if (itemKillSuccessDao.countByKillUserId(killId, userId) <= 0) {
            // 查询待秒杀商品详情
            ItemKill itemKill = itemKillDao.queryById(killId);
            // 判断是否可以被秒杀
            if (itemKill != null && itemKill.getCanKill() == 1 && itemKill.getTotal() > 0) {
                // 商品库存减一
                int res = itemKillDao.updateKillItem(killId);
                if (res > 0) {
                    // 生成秒杀成功的订单，并通知用户
                    commonRecordKillSuccessInfo(itemKill, userId);
                    result = true;
                }

            }
        } else {
            throw new Exception("您已经抢购过该商品了");
        }

        return result;
    }

    /**
     * 通用的方法-记录用户秒杀成功后生成的订单-并进行异步邮件消息的通知
     *
     * @param kill
     * @param userId
     * @throws Exception
     */
    private void commonRecordKillSuccessInfo(ItemKill kill, Integer userId) {
        ItemKillSuccess entity = new ItemKillSuccess();
        String orderCode = String.valueOf(snowFlake.nextId());

        entity.setItemId(kill.getItemId());
        entity.setKillId(kill.getId());
        entity.setCode(orderCode);
        entity.setUserId(userId.toString());
        entity.setStatus(OrderStatus.SuccessNotPayed.getCode().byteValue());
        entity.setCreateTime(DateTime.now().toDate());


        if (itemKillSuccessDao.countByKillUserId(kill.getId(), userId) <= 0) {
            int res = itemKillSuccessDao.insert(entity);
            if (res > 0) {
                // TODO 进行异步邮件消息的通知=rabbitmq+mail
                rabbitSenderService.sendKillSuccessEmailMsg(orderCode);
                // TODO 将订单发送至死信队列，实现用户如果超时未支付，则订单失效
                rabbitSenderService.sendKillSuccessDeadMsg(orderCode);
            }
        }
    }


    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 商品秒杀核心业务逻辑的处理-redis 分布式锁
     *
     * @param killId:
     * @param userId:
     * @return: java.lang.Boolean
     */
    @Override
    public Boolean killItemV2(Integer killId, Integer userId) throws Exception {
        Boolean result = false;

        if (itemKillSuccessDao.countByKillUserId(killId, userId) <= 0) {

            ValueOperations valueOperations = redisTemplate.opsForValue();
            final String key = new StringBuffer().append(killId).append(userId).append("redisLock").toString();
            final String value = snowFlake.nextId() + "";
            Boolean caceRes = valueOperations.setIfAbsent(key, value);
            if (caceRes) {
                redisTemplate.expire(key, 30, TimeUnit.SECONDS);

                try {
                    ItemKill itemKill = itemKillDao.queryById(killId);

                    if (itemKill != null && itemKill.getCanKill() == 1 && itemKill.getTotal() > 0) {
                        int res = itemKillDao.updateKillItem(killId);
                        if (res > 0) {
                            commonRecordKillSuccessInfo(itemKill, userId);
                            result = true;
                        }

                    }
                } catch (Exception e) {
                    throw new Exception("还没到抢购日期、已过了抢购时间或已被抢购完毕！");
                } finally {
                    if (value.equals(valueOperations.get(key).toString())) {
                        redisTemplate.delete(key);
                    }
                }
            }
        } else {
            throw new Exception("redis-您已经抢购过该商品了");
        }

        return result;
    }

    /**
     * 商品秒杀核心业务逻辑的处理-redis 分布式锁
     *
     * @param killId:
     * @param userId:
     * @return: java.lang.Boolean
     */
    @Override
    public Boolean killItemV3(Integer killId, Integer userId) throws Exception {
        Boolean result = false;

        ValueOperations valueOperations = redisTemplate.opsForValue();
        final String key = new StringBuffer().append(killId).append(userId).append("-RedisLock").toString();
        final String value = snowFlake.nextId() + "";
        Boolean cacheRes = valueOperations.setIfAbsent(key, value); //luna脚本提供“分布式锁服务”，就可以写在一起
        //TODO:借助Redis的原子操作实现分布式锁-对共享操作-资源进行控制
        if (cacheRes) {
            // TODO:redis部署节点宕机了 隐患
            redisTemplate.expire(key, 30, TimeUnit.SECONDS);
            try {
                if (itemKillSuccessDao.countByKillUserId(killId, userId) <= 0) {

                    ItemKill itemKill = itemKillDao.queryById(killId);
                    if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
                        int res = itemKillDao.updateKillItem(killId);
                        if (res > 0) {
                            commonRecordKillSuccessInfo(itemKill, userId);

                            result = true;
                        }
                    }
                }
            } catch (Exception e) {
                throw new Exception("还没到抢购日期、已过了抢购时间或已被抢购完毕！");
            } finally {
                if (value.equals(valueOperations.get(key).toString())) {
                    redisTemplate.delete(key);
                }
            }
        } else {
            throw new Exception("Redis-您已经抢购过该商品了!");
        }
        return result;
    }

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 基于Redisson分布式锁 
     *
     * @param killId: 
 * @param userId: 
     * @return: java.lang.Boolean
     */
    @Override
    public Boolean killItemV4(Integer killId, Integer userId) throws Exception {
        Boolean result = false;
        final String lockKey = new StringBuffer().append(killId).append(userId).append("-RedissonLock").toString();
        RLock lock = redissonClient.getLock(lockKey);
        boolean cache = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (cache) {
            try {
                if (itemKillSuccessDao.countByKillUserId(killId, userId) <= 0) {

                    ItemKill itemKill = itemKillDao.queryById(killId);
                    if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
                        int res = itemKillDao.updateKillItem(killId);
                        if (res > 0) {
                            commonRecordKillSuccessInfo(itemKill, userId);

                            result = true;
                        }
                    }
                } else {
                    throw new Exception("redisson-您已经抢购过该商品了!");
                }
            } finally {
                lock.unlock();
            }
        }

        return result;
    }

    @Autowired
    private CuratorFramework curatorFramework;
    private final static String pathPrefix = "/secondKill/zkLock/";

    /**
     * 基于ZooKeeper 分布式锁 
     *
     * @param killId: 
 * @param userId: 
     * @return: java.lang.Boolean
     */
    @Override
    public Boolean killItemV5(Integer killId, Integer userId) throws Exception {
        Boolean result = false;
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, pathPrefix + killId + userId + "zkLock");

        try {
            if (mutex.acquire(10L,TimeUnit.SECONDS)){
                // 判断用户是否已经抢购过商品，已抢购则返回false
                if (itemKillSuccessDao.countByKillUserId(killId, userId) <= 0) {
                    // 查询待秒杀商品详情
                    ItemKill itemKill = itemKillDao.queryById(killId);
                    // 判断是否可以被秒杀
                    if (itemKill != null && itemKill.getCanKill() == 1 && itemKill.getTotal() > 0) {
                        // 商品库存减一
                        int res = itemKillDao.updateKillItem(killId);
                        if (res > 0) {
                            // 生成秒杀成功的订单，并通知用户
                            commonRecordKillSuccessInfo(itemKill, userId);
                            result = true;
                        }

                    }
                } else {
                    throw new Exception("您已经抢购过该商品了");
                }
            }
        } catch (Exception e) {
            throw new Exception("还没到抢购日期、已过了抢购时间或已被抢购完毕！");
        } finally {
            if (mutex != null) {
                mutex.release();
            }
        }

        return result;
    }
}
