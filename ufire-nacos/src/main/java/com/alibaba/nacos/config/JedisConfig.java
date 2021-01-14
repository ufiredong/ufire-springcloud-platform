package com.alibaba.nacos.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @description:
 * @author: Andy
 * @time: 2020/12/31 23:24
 */
@Configuration
public class JedisConfig {
        /**
         * 创建jedis 连接池
         * */
        @Bean
        public JedisPool getJedisPool(){
            return new JedisPool("8.136.110.11",6379);
        }
}
