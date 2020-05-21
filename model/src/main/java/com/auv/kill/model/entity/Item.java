package com.auv.kill.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品表(Item)实体类
 *
 * @author makejava
 * @since 2020-05-11 18:35:19
 */
@Data
public class Item implements Serializable {
    private static final long serialVersionUID = 906649113231290123L;
    
    private Integer id;
    /**
    * 商品名
    */
    private String name;
    /**
    * 商品编号
    */
    private String code;
    /**
    * 库存
    */
    private Long stock;
    /**
    * 采购时间
    */
    private Date purchaseTime;
    /**
    * 是否有效（1=是；0=否）
    */
    private Integer isActive;
    
    private Date createTime;
    /**
    * 更新时间
    */
    private Date updateTime;


}