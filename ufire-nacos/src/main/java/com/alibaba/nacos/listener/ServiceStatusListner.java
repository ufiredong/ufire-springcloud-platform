package com.alibaba.nacos.listener;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.util.HashRingUtil;
import com.springcloud.ufire.core.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @description: 服务监听器
 * @author: fengandong
 * @time: 2020/12/31 23:40
 */
@Component
public class ServiceStatusListner {
    @Autowired
    private JedisPool jedisPool;
    //初始化监听服务上下线
    @PostConstruct
    public void init() throws Exception {
        Properties properties = System.getProperties();
        properties.setProperty("serverAddr", "127.0.0.1:8848");
        properties.setProperty("namespace", "public");
        NamingService naming = NamingFactory.createNamingService(properties);
        // 每次ufire-websocket实例发生上线事件即更新redis
        Jedis jedis = jedisPool.getResource();
        naming.subscribe(Constants.UFIRE_WEBSOCKET_REDIS_KEY, new EventListener() {
            @Override
            public void onEvent(Event event) {
                List<Instance> instances = ((NamingEvent) event).getInstances();
                jedis.del(Constants.UFIRE_WEBSOCKET_REDIS_KEY);
                for (Instance instance : instances) {
                    String realNode = instance.getIp() + ":" + instance.getPort();
                    int realNodeHash = HashRingUtil.getHash(realNode);
                    jedis.hset(Constants.UFIRE_WEBSOCKET_REDIS_KEY, String.valueOf(realNodeHash), realNode);
                }
                jedis.close();
                PublishThread publishThread = new PublishThread(jedisPool, "ufire-websocket 实例节点 down/up了");
                publishThread.start();
            }
        });
    }
}
