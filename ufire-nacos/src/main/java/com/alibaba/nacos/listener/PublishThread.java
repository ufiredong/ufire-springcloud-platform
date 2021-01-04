package com.alibaba.nacos.listener;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PublishThread extends Thread {
    private JedisPool jedisPool;
    private String message;
    private Jedis jedis;

    public PublishThread(JedisPool jedisPool, String message) {
        this.jedisPool = jedisPool;
        this.message= message;
        this.jedis=jedisPool.getResource();
    }

    @Override
    public void run() {
        try {
            //通过subscribe 的api去发布，入参是都被订阅的频道和要发送的消息
            jedis.publish("serverUpdate", message);
        } catch (Exception e) {
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }
}
