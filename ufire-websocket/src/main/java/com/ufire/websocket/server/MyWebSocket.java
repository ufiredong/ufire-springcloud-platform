package com.ufire.websocket.server;

import com.alibaba.fastjson.JSON;
import com.ufire.websocket.conf.HostEntiyConfig;
import com.ufire.websocket.util.HashRingUtil;
import com.ufire.websocket.util.LocalDateTimeUtils;
import com.ufire.websocket.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-29 15:53
 **/
@Component
@ServerEndpoint(value = "/socket/{userId}")
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    public static AtomicInteger online = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static Map<String, Session> sessionPools = new HashMap<>();

    private static Logger log = LoggerFactory.getLogger(MyWebSocket.class);

    /**
     * 发送消息方法
     *
     * @param session 客户端与socket建立的会话
     * @param message 消息
     * @throws IOException
     */
    public void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            session.getBasicRemote().sendText(message);
        }
    }


    /**
     * 连接建立成功调用
     *
     * @param session 客户端与socket建立的会话
     * @param userId  客户端的userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        Jedis jedis = getJedis();
        HostEntiyConfig myhost = (HostEntiyConfig) SpringUtil.getBean("myhost");
        MessageVo messageVo = new MessageVo();
        try {
            sessionPools.put(userId, session);
            addOnlineCount();
            int hash = HashRingUtil.getHash(userId);
            jedis.hset("user", String.valueOf(hash), userId);
            log.info("{}加入webSocket！当前人数为:{}", userId, online);
            messageVo.setDateTime(LocalDateTimeUtils.format(LocalDateTime.now()));
            messageVo.setIp(myhost.toString());
            messageVo.setType(1);
            messageVo.setContent("已连接");
            messageVo.setUserCount((online));
            sendMessage(session, JSON.toJSONString(messageVo));
        } catch (Exception e) {
            log.error("{}连接发生异常{}", userId, e.getMessage());
        } finally {
            jedis.close();
        }
    }

    /**
     * TCP长链接close事件时触发
     *
     * @param userId 关闭连接的客户端的userId
     */
    @OnClose
    public void onClose(@PathParam(value = "userId") String userId) {
        Jedis jedis = getJedis();
        try {
            sessionPools.remove(userId);
            int hash = HashRingUtil.getHash(userId);
            jedis.hdel("user", String.valueOf(hash), userId);
            subOnlineCount();
            log.info("{}断开webSocket连接！当前人数为:{}", userId, online);
        } catch (Exception e) {
            log.info("{}断开连接发生异常！{}", userId, e.getMessage());
        } finally {
            jedis.close();
        }
    }

    /**
     * 发生错误时候
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("发生错误！{}", throwable.getMessage());
        throwable.printStackTrace();
    }

    /**
     * 给指定用户发送消息
     *
     * @param userId  用户ID
     * @param message 消息
     * @throws IOException
     */
    public void sendInfo(String userId, String message) {
        Session session = sessionPools.get(userId);
        try {
            sendMessage(session, message);
        } catch (Exception e) {
            log.error("给指定用户{}发送消息发生异常！{}", userId, e.getMessage());
        }
    }


    public static void addOnlineCount() {
        online.incrementAndGet();
    }

    public static void subOnlineCount() {
        online.decrementAndGet();
    }

    public Jedis getJedis() {
        JedisPool jedisPool = SpringUtil.getBean(JedisPool.class);
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
