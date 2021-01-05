package com.springcloud.ufire.conf;
import com.springcloud.ufire.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

@Configuration
public class Subscriber extends JedisPubSub {
    private static final Logger log = LoggerFactory.getLogger(Subscriber.class);

    @Override
    public void onMessage(String channel, String message) {
        // 接受到websocket实例 变动的消息后更新 网关本地缓存 实例节点hash
        log.info("订阅监控的频道={},接受的消息为={}", channel, message);
        HashRingConfig hashRingConfig = SpringUtil.getBean(HashRingConfig.class);
        JedisPool jedisPool = SpringUtil.getBean(JedisPool.class);
        Jedis jedis = jedisPool.getResource();
        Map<String, String> serverMap = jedis.hgetAll("ufire-websocket");
        Map<String, String> userMap = jedis.hgetAll("user");
        hashRingConfig.updateHashRing(serverMap, userMap);
        //增加虚拟节点
        hashRingConfig.addVirtualNode(hashRingConfig.getHashRing());
        log.info(hashRingConfig.getHashRing().toString());
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
