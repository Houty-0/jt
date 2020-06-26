package com.jt.controller;

import com.jt.service.interfaces.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping("/list")
    public List<EasyUITree> findTreeByParentId(@RequestParam(name="id",defaultValue="0")Long parentId){

        //根据父级ID查询  直接查询数据库
        return itemCatService.findItemCatListByParentId(parentId);
        //return itemCatService.findItemCatByCache(parentId);
    }
}
