package com.jt.controller;

import com.jt.service.interfaces.ItemService;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//实现商品的业务逻辑
@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;


	@RequestMapping("/query")   //?page=1&rows=20 get请求
	public EasyUITable findItemByPage(Integer page, Integer rows) {

		return itemService.findItemByPage(page,rows);
	}
	
}
