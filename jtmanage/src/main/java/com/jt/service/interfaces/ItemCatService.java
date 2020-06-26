package com.jt.service.interfaces;

import com.jt.vo.EasyUITree;

import java.util.List;

public interface ItemCatService {

    public String findItemCatService(Long itemCatId);

    //根据parentId查询子级叶子类目信息
    List<EasyUITree> findItemCatListByParentId(Long parentId);
}
