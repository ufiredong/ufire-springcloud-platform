package com.springcloud.ufire.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {
    /**
     * 创建jedis 连接池
     * */
    @Bean
    public JedisPool getJedisPool(){
        return new JedisPool("123.57.139.87",6379);
    }
}
