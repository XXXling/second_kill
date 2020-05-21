package com.auv.kill.server.service.impl;

import com.auv.kill.model.entity.ItemKill;
import com.auv.kill.model.mapper.ItemKillDao;
import com.auv.kill.server.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/11 18:56
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemKillDao itemKillDao;

    /**
     * 获取待秒杀商品列表
     *
     
     * @return: java.util.List<com.auv.kill.model.entity.ItemKill>
     */
    @Override
    public List<ItemKill> listKilolItems() throws Exception{
        return itemKillDao.listKillItem();
    }

    /**
     * 获取商品详情
     *
     * @param id:
     * @return: com.auv.kill.model.entity.ItemKill
     */
    @Override
    public ItemKill getItemDetail(Integer id) throws Exception {
        return itemKillDao.queryById(id);
    }
}
