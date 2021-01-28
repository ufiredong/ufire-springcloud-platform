package com.ufire.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.ufire.websocket.server.MessageVo;
import com.ufire.websocket.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ufire-springcloud-platform
 * @description: XX
 * @author: fengandong
 * @create: 2020-12-29 15:48
 **/
@RestController
@RequestMapping(value = "/websocket")
public class HttpMessageController {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private MyWebSocket myWebSocket;



    @RequestMapping(value = "to/{userId}/{message}")
    public String sendMessage(@PathVariable String userId, @PathVariable String message) {
        MessageVo messageVo=new MessageVo();
        messageVo.setContent(message);
        messageVo.setType(3);
        myWebSocket.sendInfo(userId, JSON.toJSONString(messageVo));
        return "OK";
    }

    @RequestMapping(value = "getUserCount")
    public AtomicInteger getUserCount(){
        return MyWebSocket.online;
    }

}

