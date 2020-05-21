package com.auv.kill.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (RandomCode)实体类
 *
 * @author makejava
 * @since 2020-05-11 18:37:41
 */
@Data
public class RandomCode implements Serializable {
    private static final long serialVersionUID = -83285693046334988L;
    
    private Integer id;
    
    private String code;


}