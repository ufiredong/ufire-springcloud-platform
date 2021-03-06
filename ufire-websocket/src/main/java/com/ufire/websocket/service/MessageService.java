//package com.ufire.websocket.service;
//
//import com.alibaba.fastjson.JSON;
//import com.springcloud.ufire.core.model.ResetUser;
//import com.ufire.websocket.conf.MqConfig;
//import com.ufire.websocket.server.MessageVo;
//import com.ufire.websocket.server.MyWebSocket;
//import com.ufire.websocket.util.LocalDateTimeUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
///**
// * @description:
// * @author: fengandong
// * @time: 2021/1/24 3:42
// */
//@Service
//@Slf4j
//public class MessageService {
//    @Autowired
//    private MyWebSocket myWebSocket;
//
//    @RabbitListener(queues = MqConfig.QUEUE_NAME)
//    public void receiveMessage(Object object) {    // 通知 客户端 关闭 链接后重新 连接
//        log.info("shoudao-----------------------------------------{}", object);
//    }
//}
