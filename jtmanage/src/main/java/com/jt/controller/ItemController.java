package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.interfaces.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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


	@RequestMapping("/save")
	public SysResult saveItem(Item item, ItemDesc itemDesc) {

		itemService.saveItem(item,itemDesc); //100%
		return SysResult.success(); //统一异常处理

	}

	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {

		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}

	/**
	 * 1.url地址 /item/instock
	 * 2.参数: ids:100,101,102
	 * 3.返回值: 返回VO对象SysResult
	 * 下架:操作将status 改为2,同时修改updated时间
	 * 规则: 如果字符串以","进行分割,springMVC可以自动的拆分转化为数组
	 */
	@RequestMapping("/instock")
	public SysResult itemInstock(Long[] ids) {

		int status = 2;	//下架操作
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}

	///item/reshelf
	@RequestMapping("/reshelf")	//状态改为1
	public SysResult itemReshelf(Long[] ids) {

		int status = 1;	//上架操作
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}

	/**
	 * url:/item/delete
	 * 参数: ids=101,102,103
	 * 返回值结果
	 */
	@RequestMapping("/delete")
	public SysResult itemDeletes(Long[] ids) {

		itemService.deleteItems(ids);
		return SysResult.success();
	}

	/**
	 * url: /item/query/item/desc/1474392154
	 * 参数:采用restFul方式实现参数的传递
	 * 返回值: SysResult对象
	 * 业务: 根据itemId查询商品详情.
	 * @return
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {

		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}
}
