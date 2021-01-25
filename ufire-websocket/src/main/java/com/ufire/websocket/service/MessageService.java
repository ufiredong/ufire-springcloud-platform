package com.ufire.websocket.service;

import com.ufire.websocket.conf.MqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: fengandong
 * @time: 2021/1/24 3:42
 */
@Service
public class MessageService {
    @RabbitListener(queues= MqConfig.QUEUE_NAME)
    public void receiveMessage(Object object) {	// 进行消息接收处理
        System.err.println("【*** 接收消息 ***】" + object);
    }
}
