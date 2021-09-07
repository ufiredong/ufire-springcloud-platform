package com.springcloud.ufire.getway.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.springcloud.ufire.getway.utils.HashRingUtil;
import com.springcloud.ufire.getway.utils.SpringUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Properties;

/**
 * @author ufire
 */
@Component
public class ServerStatusRunner implements CommandLineRunner {
    private final String SERVICE_NAME = "ufire-websocket";
    @Override
    public void run(String... args) throws Exception {
        Properties properties=new Properties();
        properties.setProperty("serverAddr","8.136.110.11:8848");
        properties.setProperty("namespace","public");
        NamingService namingService = NamingFactory.createNamingService(properties);
        RedisTemplate redisTemplate = (RedisTemplate) SpringUtil.getBean("redisTemplate");
        try {
            namingService.subscribe(SERVICE_NAME, new EventListener() {
                @Override
                public void onEvent(Event event) {
                    List<Instance> instances = ((NamingEvent) event).getInstances();
                    redisTemplate.delete(SERVICE_NAME);
                    instances.stream().forEach(instance -> {
                        String host = instance.getIp() + ":" + instance.getPort();
                        Integer hash = HashRingUtil.getHash(host);
                        redisTemplate.opsForHash().put(SERVICE_NAME, hash.toString(), host);
                    });
                    redisTemplate.convertAndSend(SERVICE_NAME, JSON.toJSONString(instances));
                    System.out.println("监听到服务:" + SERVICE_NAME + " 发生变动" + JSON.toJSONString(instances));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
