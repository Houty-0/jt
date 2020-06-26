package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemMapper extends BaseMapper<Item>{

    @Select("SELECT * FROM tb_item ORDER BY updated DESC LIMIT #{start},#{rows}")
    List<Item> findItemByPage(int start, Integer rows);

    //sql: delete from tb_item where id in (id.....); xml配置
    //@Param作用:将参数封装为Map集合.  ids:[]值
    void deleteItems(Long[] ids);
}
