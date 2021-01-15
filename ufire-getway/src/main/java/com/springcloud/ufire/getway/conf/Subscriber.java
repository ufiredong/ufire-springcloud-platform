package com.springcloud.ufire.getway.conf;

import com.springcloud.ufire.core.constant.Constants;
import com.springcloud.ufire.core.model.ResetUser;
import com.springcloud.ufire.core.util.SpringUtil;
import com.springcloud.ufire.getway.service.ResetUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;

/**
 * @description: 监听 注册中心节点变动
 * @author: fengandong
 * @time: 2021/1/3 2:28
 */
@Configuration
public class Subscriber extends JedisPubSub {
    private static final Logger log = LoggerFactory.getLogger(Subscriber.class);
    private ThreadPoolConfig threadPoolConfig;

    @Override
    public void onMessage(String channel, String message) {
        threadPoolConfig = (ThreadPoolConfig) SpringUtil.getBean("threadPoolConfig");
        threadPoolConfig.buildThreadPool().execute(() ->
                {
                    try {
                        upDateServers(channel, message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void upDateServers(String channel, String message) throws Exception {
        // 接受到websocket实例 变动的消息后更新 网关本地缓存 实例节点hash
        log.info("订阅监控的频道={},接受的消息为={}", channel, message);
        HashRingConfig hashRingConfig = SpringUtil.getBean(HashRingConfig.class);
        DiscoveryClient discoveryClient = SpringUtil.getBean(DiscoveryClient.class);
        ResetUserService resetUserService = SpringUtil.getBean(ResetUserService.class);
        JedisPool jedisPool = SpringUtil.getBean(JedisPool.class);
        Jedis jedis = jedisPool.getResource();
        Map<String, String> serverMap = jedis.hgetAll(Constants.UFIRE_WEBSOCKET_REDIS_KEY);
        Map<String, String> userMap = jedis.hgetAll(Constants.UFIRE_WEBSOCKET_REDIS_KEY);
        List<ServiceInstance> instances = discoveryClient.getInstances(Constants.UFIRE_WEBSOCKET_REDIS_KEY);
        hashRingConfig.setLastTimeInstances(hashRingConfig.getInstances());
        hashRingConfig.setInstances(instances);
        hashRingConfig.updateHashRing(serverMap, userMap);
        //增加虚拟节点
        hashRingConfig.addVirtualNode(hashRingConfig.getHashRing());
        log.info("本次节点变动-虚拟节点插入完毕 {} ", hashRingConfig.getHashRing().getServerMap());
        log.info("上次节点变动 {} ", hashRingConfig.getHashRing().getLastTimeServerMap());
        List<ResetUser> resetUserList = hashRingConfig.getResetUserList();
        resetUserService.resetUserSend(resetUserList);
    }

    @Override
    public void onPMessage(String s, String s1, String s2) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {    //订阅了频道会调用
        log.info(String.format("redis频道订阅成功, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onPUnsubscribe(String s, int i) {

    }

    @Override
    public void onPSubscribe(String s, int i) {

    }

}
