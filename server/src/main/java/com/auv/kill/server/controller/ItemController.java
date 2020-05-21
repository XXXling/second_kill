package com.auv.kill.server.controller;

import com.auv.kill.model.entity.ItemKill;
import com.auv.kill.server.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/11 17:21
 */
@Controller
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    private static final String errorPath ="redirect:/base/error";

    @Autowired
    private ItemService itemService;

    /**
     * 查询可秒杀的商品列表 
     *
     * @param modelMap: 
     * @return: java.lang.String
     */
    @RequestMapping(value = {"/","/index","/item/list","/item/index.html"}, method = RequestMethod.GET)
    public String listItem(ModelMap modelMap){
        try {
            List<ItemKill> list = itemService.listKilolItems();
            modelMap.put("list", list);
            log.info("商品秒杀商品信息:{}",list);
        } catch (Exception e) {
            log.error("获取秒杀商品列表失败，case:{}", e.fillInStackTrace());
            return errorPath;
        }
        return "list";
    }

    /**
     * 查询秒杀商品详情 
     *
     * @param id: 
 * @param modelMap: 
     * @return: java.lang.String
     */
    @RequestMapping(value = "/item/detail/{id}",method = RequestMethod.GET)
    public String itemDetail(@PathVariable Integer id, ModelMap modelMap){
        if (id == null) {
            return errorPath;
        }

        try {
            ItemKill itemKill = itemService.getItemDetail(id);
            modelMap.put("detail",itemKill);
        } catch (Exception e) {
            log.error("获取商品详情失败。case:{}", e.fillInStackTrace());
            return errorPath;
        }

        return "info";
    }
}
