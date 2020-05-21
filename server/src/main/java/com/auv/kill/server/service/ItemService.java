package com.auv.kill.server.service;

import com.auv.kill.model.entity.ItemKill;

import java.util.List;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/11 17:30
 */
public interface ItemService {
    List<ItemKill> listKilolItems() throws Exception;

    ItemKill getItemDetail(Integer id) throws Exception;
}
