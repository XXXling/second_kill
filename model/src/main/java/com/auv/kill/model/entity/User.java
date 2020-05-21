package com.auv.kill.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表(User)实体类
 *
 * @author makejava
 * @since 2020-05-11 18:37:41
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 136188902020737516L;
    
    private Integer id;
    /**
    * 用户名
    */
    private String userName;
    /**
    * 密码
    */
    private String password;
    /**
    * 手机号
    */
    private String phone;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 是否有效(1=是；0=否)
    */
    private Integer isActive;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 更新时间
    */
    private Date updateTime;


}