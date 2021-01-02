package com.springcloud.ufire.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @description:
 * @author: Andy
 * @time: 2021/1/2 19:54
 */
@Component
public class Subscriber extends Thread {
    @Autowired
    private Jedis jedis;
    private final RedisMsgPubSubListener redisMsgPubSubListener;

    private String channelName;

    public Subscriber(RedisMsgPubSubListener redisMsgPubSubListener) {
        super();
        this.redisMsgPubSubListener = redisMsgPubSubListener;
        this.channelName = "serverUpdate";
    }

    @Override
    public void run() {
        try {
            jedis.subscribe(redisMsgPubSubListener, channelName);// 通过jedis.subscribe()方法去订阅，入参是1.订阅者、2.频道名称
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("频道订阅失败：%s", e));
        } finally {
            if (null != jedis)
                jedis.close();
        }
    }


}
