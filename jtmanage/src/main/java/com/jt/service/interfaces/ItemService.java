package com.jt.service.interfaces;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

    EasyUITable findItemByPage(Integer page, Integer rows);

    void saveItem(Item item,ItemDesc itemDesc);

    void updateItem(Item item,ItemDesc itemDesc);

    void updateStatus(Long[] ids, Integer status);

    void deleteItems(Long[] ids);

    ItemDesc findItemDescById(Long itemId);

    Item findItemById(Long itemId);
}
