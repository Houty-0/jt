package com.jt.service.dubboimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.UUID;

@Service
public class DubboUserServiceImpl implements DubboUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JedisCluster jedisCluster;

    //RPC调用反序列化之后的结果User可以直接获取
    //用户入库时,必须传递email信息,否则程序报错,暂时使用电话代替
    //密码加密:md5加密,md5hash方式 盐值
    @Override
    public void saveUser(User user) {
        String md5Password =
                DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password)
                .setEmail(user.getPhone())
                .setCreated(new Date())
                .setUpdated(user.getCreated());
        userMapper.insert(user);
    }

    @Override
    public String findUserByUP(User user) {
        //注册和登录的密码加密必须一致.
        String md5Password =
                DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        //根据对象中不为null的属性充当where条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
        User userDB = userMapper.selectOne(queryWrapper);
        if(userDB==null) {
            //根据用户名或密码错误
            return null;
        }
        //表示用户数据是有效的.需要进行单点登录操作
        String ticket = UUID.randomUUID().toString();
        //脱敏处理  user用户名 密码 身份证号 家庭地址 户籍信息 电话号码 金额
        userDB.setPassword("123456hahaha");//伪造数据
        String userJSON = ObjectMapperUtil.toJSON(userDB);
        jedisCluster.setex(ticket,7*24*3600,userJSON);
        return ticket;
    }
}
