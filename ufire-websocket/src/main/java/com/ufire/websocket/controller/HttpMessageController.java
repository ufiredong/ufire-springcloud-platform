package com.ufire.websocket.controller;

import com.ufire.websocket.server.MessageVo;
import com.ufire.websocket.server.MyWebSocket;
import com.ufire.websocket.util.LocalDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
        System.out.println(serverPort);
        // 心跳检查  heartCheck
        if("heartCheck".equals(message)){
            myWebSocket.sendInfo(userId, "");
        }else{
            myWebSocket.sendInfo(userId, message);
        }
        return "OK";
    }
    @RequestMapping(value = "close")
    public void   close(@RequestParam("userId") String userId){

        myWebSocket.onClose(userId);
    }

}

