package com.jt.web.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.interfaces.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/item")
public class WebItemController {

    @Autowired
    private ItemService itemService;
    /**
     * 根据前台url动态获取商品数据
     * http://manage.jt.com/web/item/findItemById?itmeId=562379
     */

    @RequestMapping("/findItemById")
    public Item findItemById(Long itemId) {
        Item item = itemService.findItemById(itemId);
        return item;
    }

    /**
     * 根据itemId动态获取商品详情信息
     */
    @RequestMapping("/findItemDescById")
    public ItemDesc findItemDescById(Long itemId) {

        return itemService.findItemDescById(itemId);
    }

}
