package com.auv.kill.model.dto;

import com.auv.kill.model.entity.ItemKillSuccess;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/13 18:27
 */
@Data
@ToString
public class KillSuccessUserInfo extends ItemKillSuccess implements Serializable {
    private String userName;

    private String phone;

    private String email;

    private String itemName;

    @Override
    public String toString() {
        return super.toString()+"\nKillSuccessUserInfo{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
