package com.springcloud.ufire.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * @description: 频道订阅
 * @author: fengandong
 * @time: 2021/1/3 2:24
 */
@Slf4j
@Configuration
public class SubEventConfing implements CommandLineRunner {
    @Autowired
    JedisPool jedisPool;

    @Override
    public void run(String... args) throws Exception {
        log.info("初始化启动redis频道订阅");
        //订阅者
        SubThread subThread = new SubThread(jedisPool, new Subscriber());
        subThread.start();
    }

    @Bean
    public ThreadPoolConfig getThreadPoolConfig() {
        return new ThreadPoolConfig();
    }

}
