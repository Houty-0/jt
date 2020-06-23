package com.jt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemCatMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemCat;
import com.jt.service.interfaces.ItemService;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ItemCatMapper itemCatMapper;


	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		//2.利用MP方式实现分页查询
		IPage<Item> iPage = new Page<>(page, rows);	//查询页数和条数
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		IPage<Item> resultPage = itemMapper.selectPage(iPage, queryWrapper);
		//查询总页数
		int total = (int) resultPage.getTotal();
		//查询分页信息
		List<Item> itemList = resultPage.getRecords();
		if(!CollectionUtils.isEmpty(itemList)){
			for (Item item:itemList){
				if(item.getCid()!=null){
					ItemCat itemCat = itemCatMapper.selectById(item.getCid());
					if(itemCat!=null){
						item.setCatName(itemCat.getName());
					}

				}
			}
		}

		return new EasyUITable(total, itemList);
	}
}
