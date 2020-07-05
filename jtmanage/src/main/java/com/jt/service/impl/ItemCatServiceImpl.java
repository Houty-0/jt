package com.jt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.annotation.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.service.interfaces.ItemCatService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    //@Autowired
    private Jedis jedis;//注入redisAPI对象


    @CacheFind
    @Override
    public String findItemCatService(Long itemCatId) {
        ItemCat itemCat = itemCatMapper.selectById(itemCatId);
        if(itemCat!=null){
            return itemCat.getName();
        }
        return null;
    }

    @CacheFind
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

    @Override
    public List<EasyUITree> findItemCatByCache(Long parentId) {
        Long startTime = System.currentTimeMillis();
        List<EasyUITree> treeList = new ArrayList<EasyUITree>();
        String key = "com.jt.service.impl.ItemCatServiceImpl.findItemCatByCache::"+parentId;
        String value = jedis.get(key);	//根据key,查询值
        if(StringUtils.isEmpty(value)) {
            //缓存中没有数据,查询数据库.需要将数据存入redis.
            treeList = findItemCatListByParentId(parentId);
            String json = ObjectMapperUtil.toJSON(treeList);
            //将数据保存到redis中.
            jedis.set(key, json);
            Long dbTime = System.currentTimeMillis();
            System.out.println("查询数据库执行时间:"+(dbTime-startTime)+"毫秒");
        }else {
            //缓存中有数据,需要将JSON转化为对象
            treeList = ObjectMapperUtil.toObj(value, treeList.getClass());
            Long cacheTiemt = System.currentTimeMillis();
            System.out.println("查询缓存时间:"+(cacheTiemt-startTime)+"毫秒");
        }
        return treeList;
    }
}
