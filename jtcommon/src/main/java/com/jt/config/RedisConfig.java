package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.Jedis;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

    @Value("${redis.node}")
    private String redisNode;   //IP:PORT

    @Bean    //标识实例化对象的类型
    @Scope("prototype")	//对象的多例  使用链接池
    public Jedis jedis() {

        String[] nodeArray = redisNode.split(":");
        String host = nodeArray[0];
        int port = Integer.parseInt(nodeArray[1]);
        return new Jedis(host, port);
    }
}
