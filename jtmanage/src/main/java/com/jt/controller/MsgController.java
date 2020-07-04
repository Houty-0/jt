package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {

    //如果动态获取当前端口号????  从配置文件中
    @Value("${server.port}")
    private Integer port;


    @RequestMapping("/getMsg")
    public String getMsg() {

        return "当前访问端口"+port+"!!!";
    }
}
