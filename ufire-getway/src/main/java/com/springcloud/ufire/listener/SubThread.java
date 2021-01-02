package com.springcloud.ufire.listener;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import rx.Subscriber;

/**
 * @description:
 * @author: Andy
 * @time: 2021/1/1 23:09
 */
public class SubThread extends Thread {
//    private final JedisPool jedisPool;
//    private final Subscriber subscriber = new Subscriber();
//
//    private final String channel = "mychannel";
//
//    public SubThread(JedisPool jedisPool) {
//        super("SubThread");
//        this.jedisPool = jedisPool;
//    }

//    @Override
//    public void run() {
//        System.out.println(String.format("subscribe redis, channel %s, thread will be blocked", channel));
//        Jedis jedis = null;
//        try {
//            jedis = jedisPool.getResource();   //取出一个连接
//            jedis.subscribe(subscriber, channel);    //通过subscribe 的api去订阅，入参是订阅者和频道名
//        } catch (Exception e) {
//            System.out.println(String.format("subsrcibe channel error, %s", e));
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
}
