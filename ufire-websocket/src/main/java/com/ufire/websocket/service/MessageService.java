package com.ufire.websocket.service;

import com.springcloud.ufire.core.model.ResetUser;
import com.ufire.websocket.conf.MqConfig;
import com.ufire.websocket.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: fengandong
 * @time: 2021/1/24 3:42
 */
@Service
@Slf4j
public class MessageService {
    @Autowired
    private MyWebSocket myWebSocket;
    @RabbitListener(queues= MqConfig.QUEUE_NAME)
    public void receiveMessage(ResetUser resetUser) {	// 进行消息接收处理
        log.info(resetUser.toString());
        myWebSocket.sendInfo(resetUser.getUserId(),"close");
    }

}
