package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller    //返回的是页面
@RequestMapping("/cart")
public class CartController {

    @Reference(check = false)
    private DubboCartService cartService;

    @RequestMapping("/show")
    public String showCart(Model model, HttpServletRequest request){

        User user = ThreadLocalUtil.getUser();
        Long userId = user.getId();
        List<Cart> cartList = cartService.findCartListByUserId(userId);

        model.addAttribute("cartList",cartList);

        return "cart";
    }


    /**
     * 	完成购物车新增
     * url:http://www.jt.com/cart/add/562379.html
     *   参数:form表单提交 itemId/itemTitle.....
     *   返回值: 页面逻辑名称 重定向到购物车展现页面
     * 参数接收:如果参数名称与属性相同则可以直接转化
     */
    @RequestMapping("/add/{itemId}")
    public String saveCart(Cart cart) {
        User user = ThreadLocalUtil.getUser();
        Long userId = user.getId();
        cart.setUserId(userId);
        cartService.saveCart(cart);
        return "redirect:/cart/show.html";
    }

    /**
     * 实现购物车数量更新
     * url:http://www.jt.com/cart/update/num/562379/24
     * 参数:itemId,num
     * 返回值信息:SysResult对象 JSON串
     */
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public SysResult updateNum(Cart cart) {

        Long userId = ThreadLocalUtil.getUser().getId();
        cart.setUserId(userId);
        cartService.updateNum(cart);
        return SysResult.success();
    }


    /**
     * 实现购物车删除
     * 跳转页面:重定向到购物车列表页面
     * url:http://www.jt.com/cart/delete/1474392156.html
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteCart(Cart cart) {

        //Long userId = ThreadLocalUtil.getUser().getId();
        Long userId = 7L;
        cart.setUserId(userId);
        cartService.deleteCart(cart);
        return "redirect:/cart/show.html";
    }

}
