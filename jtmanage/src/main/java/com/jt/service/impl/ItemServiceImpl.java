package com.jt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.annotation.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemCat;
import com.jt.pojo.ItemDesc;
import com.jt.service.interfaces.ItemService;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ItemCatMapper itemCatMapper;

	@Autowired
	private ItemDescMapper itemDescMapper;

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

			return new EasyUITable(total, itemList);
		}
		return null;
	}

	@Transactional    //控制事务
	@Override
	public void saveItem(Item item, ItemDesc itemDesc) {
		item.setStatus(1)  //上架状态
				.setCreated(new Date())
				.setUpdated(item.getCreated()); //时间相同
		itemMapper.insert(item);	//利用mp直接入库

		//MP操作时,当item入库之后会将主键自动的回显.

		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Transactional
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);

		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	public void updateStatus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status)
				.setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<Item>();
		//将数组转化为List集合
		List<Long> idList = Arrays.asList(ids);
		updateWrapper.in("id", idList);
		itemMapper.update(item, updateWrapper);
	}

	@Override
	public void deleteItems(Long[] ids) {
		//1.利用MP方式实现数据传递
		//List<Long> idList = Arrays.asList(ids);
		//itemMapper.deleteBatchIds(idList);

		//2.利用xml配置文件,实现数据的删除. 切记不用与MP方法重名
		itemMapper.deleteItems(ids);

		//利用MP实现ItemDesc数据的删除.
		List<Long> idList = Arrays.asList(ids);

		itemDescMapper.deleteBatchIds(idList);

	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectById(itemId);
	}
}
