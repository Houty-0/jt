package com.jt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//为用户提供JSON数据
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getMsg")
    public String getMsg(){

        return "创建单点登陆项目成功";
    }

}
