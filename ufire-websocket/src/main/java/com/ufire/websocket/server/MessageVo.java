package com.ufire.websocket.server;

import lombok.Data;

@Data
public class MessageVo {

    private String dateTime;

    private Integer type;

    private String content;

    private String from;

    private String ip;
}
