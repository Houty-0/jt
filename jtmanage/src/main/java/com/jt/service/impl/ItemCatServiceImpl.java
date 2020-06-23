package com.jt.service.impl;

import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.service.interfaces.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;


    @Override
    public String findItemCatService(Long itemCatId) {
        ItemCat itemCat = itemCatMapper.selectById(itemCatId);

        return itemCat.getName();
    }
}
