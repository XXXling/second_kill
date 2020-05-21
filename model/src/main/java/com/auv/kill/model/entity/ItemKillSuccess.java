package com.auv.kill.model.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 秒杀成功订单表(ItemKillSuccess)实体类
 *
 * @author makejava
 * @since 2020-05-11 18:37:41
 */
@Data
@ToString
public class ItemKillSuccess implements Serializable {
    private static final long serialVersionUID = -28593013574485272L;
    /**
    * 秒杀成功生成的订单编号
    */
    private String code;
    /**
    * 商品id
    */
    private Integer itemId;
    /**
    * 秒杀id
    */
    private Integer killId;
    /**
    * 用户id
    */
    private String userId;
    /**
    * 秒杀结果: -1无效  0成功(未付款)  1已付款  2已取消
    */
    private Byte status;
    /**
    * 创建时间
    */
    private Date createTime;

    private Integer diffTime;

}