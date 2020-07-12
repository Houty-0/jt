package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    //rpc调用 不要求必须先启动服务提供者
    @Reference(check=false)
    private DubboUserService userService;

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * http://www.jt.com/user/login.html
     * http://www.jt.com/user/register.html
     * @return
     */
    @RequestMapping("/{moduleName}")
    public String module(@PathVariable String moduleName) {

        return moduleName;
    }

    /**
     * 	用户新增
     * 1.url:/user/doRegister
     * 2.参数:password: admin123  username: admin999 phone: 13122223333
     * 3.返回值: SysResult对象
     */
    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult saveUser(User user) {

        //user----JSON-----user
        //user----序列化[010111010010110]----反序列化User
        userService.saveUser(user);
        return SysResult.success();
    }

    /**
     * 完成用户单点登录操作.
     * 1.url:http://www.jt.com/user/doLogin?r=0.7185631650250299
     * 2.参数:username: admin1811,password: admin123456
     * 3.返回值SysResult对象
     *
     * 业务:校验用户信息,之后返回数据
     * 	ticket=uuid
     * 	ticket=null;
     */
    @RequestMapping("/doLogin")
    @ResponseBody	//返回json.
    public SysResult findUserByUP(User user, HttpServletResponse response) {

        String ticket = userService.findUserByUP(user);
        if(StringUtils.isEmpty(ticket)) {
            return SysResult.fail();
        }

        //如果ticket信息里边有值,则将数据保存到cookie中
        //www.jt.com manage.jt.com  sso.jt.com
        Cookie cookie = new Cookie("JT_TICKET", ticket);
        cookie.setDomain("jt.com"); //设定cookie的共享 在jt.com的域名中实现共享
        cookie.setPath("/");  //cookie的权限 一般都写/
        cookie.setMaxAge(7*24*3600); //7天超时
        response.addCookie(cookie);
        return SysResult.success();
    }


    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        String ticket = CookieUtil.getCookieValue(request, "JT_TICKET");
        //2.判断是否成功获取ticket信息
        if (!StringUtils.isEmpty(ticket)) {
            jedisCluster.del(ticket);    //删除redis中的数据.
            CookieUtil.deleteCookie(response, "JT_TICKET", "jt.com", "/");
        }
        return "redirect:/";    //跳转系统首页
    }
}
