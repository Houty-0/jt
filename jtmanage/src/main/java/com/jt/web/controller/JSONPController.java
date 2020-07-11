package com.jt.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONPController {

    //@RequestMapping("/web/testJSONP")
    public String jsonp(String callback) {

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(1000L).setItemDesc("商品详情信息");
        //JSON转化需要自己手动完成
        String json = ObjectMapperUtil.toJSON(itemDesc);
        return callback+"("+json+")";

        //return callback+"({'id':'1','name':'tomcat猫'})";
    }

    /**
     * JSONPObject是跨域访问的API
     * function:回调函数的名称
     * value:   需要返回的对象
     * @param callback
     * @return
     */
    @RequestMapping("/web/testJSONP")
    public JSONPObject jsonp2(String callback) {

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(1000L).setItemDesc("商品详情信息");
        return new JSONPObject(callback, itemDesc);
    }
}
