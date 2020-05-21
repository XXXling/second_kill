package com.auv.kill.model.mapper;

import com.auv.kill.model.entity.ItemKill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 待秒杀商品表(ItemKill)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-11 18:37:41
 */
@Mapper
public interface ItemKillDao {

    /**
     * 通过ID查询查询待秒杀商品详情
     *
     * @param id 主键
     * @return 实例对象
     */
    ItemKill queryById(Integer id);


    /**
     * 查询待秒杀商品列表
     *
     * @return: java.util.List<com.auv.kill.model.entity.ItemKill>
     */
    List<ItemKill> listKillItem();

    /**
     * 更新秒杀商品库存
     *
     * @param killId:
     * @return: void
     */
    int updateKillItem(@Param("killId") Integer killId);
}