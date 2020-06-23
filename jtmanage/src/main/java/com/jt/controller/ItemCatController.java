package com.jt.controller;

import com.jt.service.interfaces.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据id查询商品分类名称
     * url:/item/cat/queryItemName
     *   参数: itemCatId:val
     *   结果: 商品分类名称
     */
    @RequestMapping("/queryItemName")
    public String findItemCatNameById(Long itemCatId) {

        return itemCatService.findItemCatService(itemCatId);
    }
}
