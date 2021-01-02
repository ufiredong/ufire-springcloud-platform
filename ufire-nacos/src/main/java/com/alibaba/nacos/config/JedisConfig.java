package com.alibaba.nacos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * @description:
 * @author: Andy
 * @time: 2020/12/31 23:24
 */
@Configuration
public class JedisConfig {
    @Bean
    Jedis getJedis() {
        return new Jedis("123.57.139.87", 6379);
    }
}
