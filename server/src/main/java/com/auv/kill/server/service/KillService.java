package com.auv.kill.server.service;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/12 16:19
 */
public interface KillService {

    /**
     * 秒杀核心业务逻辑
     *
     * @param killId:
 * @param userId:
     * @return: java.lang.Boolean
     */
    Boolean killItem(Integer killId, Integer userId) throws Exception;

    /**
     * 基于redis分布式锁
     * SETNX + EXPIRE联合使用
     * @param killId:
     * @param userId:
     * @return: java.lang.Boolean
     */
    Boolean killItemV2(Integer killId, Integer userId) throws Exception;

    /**
     * 基于redis分布式锁
     * SETNX + EXPIRE联合使用
     * @param killId:
 * @param userId: 
     * @return: java.lang.Boolean
     */
    Boolean killItemV3(Integer killId, Integer userId) throws Exception;
    
    /**
     * 基于redisson-分布式锁 
     *
     * @param killId:
 * @param userId: 
     * @return: java.lang.Boolean
     */
    Boolean killItemV4(Integer killId, Integer userId) throws Exception;

    /**
     * 基于ZooKeeper的分布式锁
     *
     * @param killId:
     * @param userId:
     * @return: java.lang.Boolean
     */
    Boolean killItemV5(Integer killId, Integer userId) throws Exception;
}
