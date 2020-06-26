package com.jt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.service.interfaces.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;


    @Override
    public String findItemCatService(Long itemCatId) {
        ItemCat itemCat = itemCatMapper.selectById(itemCatId);

        return itemCat.getName();
    }

    @Override
    public List<EasyUITree> findItemCatListByParentId(Long parentId) {
        //1.根据parentID查询数据库记录.
        List<ItemCat> itemCatList = findItemCatList(parentId);

        //2.itemCatList~~~~List<EasyUITree>
        List<EasyUITree> treeList = new ArrayList<>(itemCatList.size());
        if(!CollectionUtils.isEmpty(itemCatList)){
            for (ItemCat itemCat : itemCatList) {
                Long id = itemCat.getId();
                String text = itemCat.getName();
                //如果是父级 默认closed,否则开启open
                String state = itemCat.getIsParent()?"closed":"open";
                EasyUITree uiTree = new EasyUITree(id, text, state);
                treeList.add(uiTree);
            }
        }

        return treeList;
    }

    //根据parentId查询分类信息
    private List<ItemCat> findItemCatList(Long parentId) {

        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        return itemCatMapper.selectList(queryWrapper);
    }
}
