package com.auv.kill.model.mapper;

import com.auv.kill.model.dto.KillSuccessUserInfo;
import com.auv.kill.model.entity.ItemKillSuccess;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 秒杀成功订单表(ItemKillSuccess)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-11 18:37:41
 */
public interface ItemKillSuccessDao {

    /**
     * 通过ID查询单条数据
     *
     * @param code 主键
     * @return 实例对象
     */
    ItemKillSuccess queryById(String code);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ItemKillSuccess> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param itemKillSuccess 实例对象
     * @return 对象列表
     */
    List<ItemKillSuccess> queryAll(ItemKillSuccess itemKillSuccess);

    /**
     * 新增数据
     *
     * @param itemKillSuccess 实例对象
     * @return 影响行数
     */
    int insert(ItemKillSuccess itemKillSuccess);

    /**
     * 修改数据
     *
     * @param itemKillSuccess 实例对象
     * @return 影响行数
     */
    int update(ItemKillSuccess itemKillSuccess);

    /**
     * 通过主键删除数据
     *
     * @param code 主键
     * @return 影响行数
     */
    int deleteById(String code);

    /**
     * 判断用户是否已经抢购过秒杀商品
     *
     * @param killId:
    * @param userId:
     * @return: java.lang.Integer
     */
    Integer countByKillUserId(@Param("killId") Integer killId, @Param("userId") Integer userId);

    /**
     * 根据订单号查询秒杀成功订单详情 
     *
     * @param orderNo: 
     * @return: com.auv.kill.model.dto.KillSuccessUserInfo
     */
    KillSuccessUserInfo findByCode(@Param("orderNo") String orderNo);

    /**
     * 用户超时未支付，变更订单失效
     *
     * @param code:
     * @return: void
     */
    void expireOrder(@Param("code") String code);

    /**
     * 定时任务批量扫描超时未支付订单
     *

     * @return: java.util.List<com.auv.kill.model.entity.ItemKillSuccess>
     */
    List<ItemKillSuccess> selectExpireOrders();
}