//package com.springcloud.ufire.getway.conf;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//
///**
// * @description: 订阅serverUpdate频道
// * @author: fengandong
// * @time: 2021/1/3 2:28
// */
//public class SubThread extends Thread {
//    private JedisPool jedisPool;
//    private Subscriber subscriber;
//
//    public final String channel = "serverUpdate";
//
//    public SubThread(JedisPool jedisPool, Subscriber subscriber) {
//        this.jedisPool = jedisPool;
//        this.subscriber = subscriber;
//    }
//
//    @Override
//    public void run() {
//        Jedis jedis = null;
//        try {
//            //取出一个连接
//            jedis = jedisPool.getResource();
//            //通过subscribe 的api去订阅，入参是订阅者和频道名
//            jedis.subscribe(subscriber, channel);
//        } catch (Exception e) {
//        } finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//    }
//}
