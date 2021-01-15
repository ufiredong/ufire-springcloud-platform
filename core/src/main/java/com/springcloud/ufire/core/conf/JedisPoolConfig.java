package com.springcloud.ufire.core.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * @description:
 * @author: Andy
 * @time: 2021/1/2 20:00
 */
@Configuration
public class JedisPoolConfig {
    @Bean
    JedisPool getJedis() {
        return new JedisPool("8.136.110.11", 6379);
    }
}
