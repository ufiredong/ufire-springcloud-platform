package com.ufire.websocket.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.InetAddress;
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
    @Value("${server.port}")
    private String serverPort;
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger online = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static Map<String, Session> sessionPools = new HashMap<>();


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
     * @param userId  客户端的userName
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        sessionPools.put(userId, session);
        addOnlineCount();
        System.out.println(userId + "加入webSocket！当前人数为" + online);
        try {
            System.out.println(session + "欢迎" + userId + "加入连接！");
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            sendMessage(session, "『"+hostAddress+"』");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接时调用
     *
     * @param userName 关闭连接的客户端的姓名
     */
    @OnClose
    public void onClose(@PathParam(value = "name") String userName) {
        sessionPools.remove(userName);
        subOnlineCount();
        System.out.println(userName + "断开webSocket连接！当前人数为" + online);
    }

    /**
     * 收到客户端消息时触发（群发）
     *
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        for (Session session : sessionPools.values()) {
            try {
                sendMessage(session, message);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
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
        System.out.println("发生错误");
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
            e.printStackTrace();
        }
    }


    public static void addOnlineCount() {
        online.incrementAndGet();
    }

    public static void subOnlineCount() {
        online.decrementAndGet();
    }
}
