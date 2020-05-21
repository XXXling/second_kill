package com.auv.kill.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 待秒杀商品表(ItemKill)实体类
 *
 * @author makejava
 * @since 2020-05-11 18:37:41
 */
@Data
public class ItemKill implements Serializable {
    private static final long serialVersionUID = 261702526620984008L;
    /**
    * 主键id
    */
    private Integer id;
    /**
    * 商品id
    */
    private Integer itemId;
    /**
    * 可被秒杀的总数
    */
    private Integer total;
    /**
    * 秒杀开始时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    /**
    * 秒杀结束时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    /**
    * 是否有效（1=是；0=否）
    */
    private Byte isActive;
    /**
    * 创建的时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String itemName;
    /**
     *采用服务器时间控制是否可以进行抢购 非数据库字段
     */
    private Integer canKill;


}