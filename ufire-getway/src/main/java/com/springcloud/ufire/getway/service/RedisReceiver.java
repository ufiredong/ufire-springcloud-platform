package com.springcloud.ufire.getway.service;

import org.springframework.stereotype.Service;

@Service
public class RedisReceiver {
    public void receiveMessage(String message) {
        System.out.println("消息处理器1>我处理用户信息："+message);
        //这里是收到通道的消息之后执行的方法
        //此处执行接收到消息后的 业务逻辑
    }
}
