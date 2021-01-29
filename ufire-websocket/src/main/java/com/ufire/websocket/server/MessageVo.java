package com.ufire.websocket.server;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class MessageVo {

    private String dateTime;

    private Integer type;

    private String content;

    private AtomicInteger userCount;

    private String from;

    private String ip;
}
