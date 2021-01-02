package com.alibaba.nacos.listener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.util.HashRingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @description: 服务监听器
 * @author: fengandong
 * @time: 2020/12/31 23:40
 */
@Component
public class ServiceStatusListner {
    private static final Logger logger = LoggerFactory.getLogger(ServiceStatusListner.class);
    @Autowired
    private Jedis jedis;
    private Map<Integer, String> instanceHashMap = new HashMap<>();

    private static final String SERVER_WEBSOCKET = "ufire-websocket";

    //初始化监听服务上下线
    @PostConstruct
    public void init() throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("serverAddr", "127.0.0.1:8848");
        properties.setProperty("namespace", "public");
        NamingService naming = NamingFactory.createNamingService(properties);
        List<String> serviceNames = new ArrayList<>();
        serviceNames.add(SERVER_WEBSOCKET);
        // 每次ufire-websocket实例发生上线事件即更新redis
        for (String serviceName : serviceNames) {
            naming.subscribe(serviceName, new EventListener() {
                @Override
                public void onEvent(Event event) {
                    List<Instance> instances = ((NamingEvent) event).getInstances();
                    String serviceName = ((NamingEvent) event).getServiceName();
                    jedis.del(SERVER_WEBSOCKET);
                    for (Instance instance : instances) {
                        String hostPort = instance.getIp() + ":" + instance.getPort();
                        int hash = HashRingUtil.getHash(hostPort);
                        jedis.hset(SERVER_WEBSOCKET, hostPort, String.valueOf(hash));
                    }
                    jedis.publish("serverUpdate", jedis.get(SERVER_WEBSOCKET));
                }
            });
        }
    }


}
