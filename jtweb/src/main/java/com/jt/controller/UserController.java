package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * http://www.jt.com/user/login.html
     * http://www.jt.com/user/register.html
     * @return
     */
    @RequestMapping("/{moduleName}")
    public String module(@PathVariable String moduleName) {

        return moduleName;
    }
}
