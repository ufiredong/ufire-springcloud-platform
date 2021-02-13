package com.ufire.websocket.scheduled;


import com.alibaba.fastjson.JSON;
import com.ufire.websocket.conf.HostEntiyConfig;
import com.ufire.websocket.server.MessageVo;
import com.ufire.websocket.server.MyWebSocket;
import com.ufire.websocket.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 1、定时任务每隔5s向web端发送一次当前服务内usercount有效用户在线数量
 * 2、获取当前sessionPool内第一个有效的用户发送信息
 */
@Component
@Slf4j
public class SendUserCountTask {
    public static AtomicInteger online = MyWebSocket.online;
    Map<String, Session> sessionPools = MyWebSocket.sessionPools;
    @Autowired
    private MyWebSocket myWebSocket;

    @Scheduled(cron = "0/5 * * * * ? ") // 间隔5秒执行
    public void sendUserCount() {
        log.info("用户数量统计----定时任务开始执行-----");
        HostEntiyConfig myhost = (HostEntiyConfig) SpringUtil.getBean("myhost");
        Optional<Map.Entry<String, Session>> userSession = sessionPools.entrySet().stream().findFirst();
        userSession.ifPresent(new Consumer<Map.Entry<String, Session>>() {
            @Override
            public void accept(Map.Entry<String, Session> stringSessionEntry) {
                String userId = stringSessionEntry.getKey();
                MessageVo messageVo = new MessageVo();
                messageVo.setUserCount(online);
                messageVo.setIp(myhost.toString());
                messageVo.setType(4);
                myWebSocket.sendInfo(userId, JSON.toJSONString(messageVo));
            }
        });
        log.info("当前服务用户数量:{}", online);
        log.info("用户数量统计----定时任务执行结束-----");
    }

}
