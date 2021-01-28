package com.ufire.websocketui.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: fengandong
 * @time: 2021/1/21 22:58
 */
@Data
public class ContainerVo {
    private String id;
    private String name;
    private String ip;
    private String status;
    private String state;
    private String port;
    private LocalDateTime creatTime;
    private String operat;
    private Integer userCount;
}
