package com.springcloud.ufire.getway.conf;
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
        return new JedisPool("127.0.0.1", 6379);
    }
}
