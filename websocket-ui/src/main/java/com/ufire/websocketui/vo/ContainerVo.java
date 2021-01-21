package com.ufire.websocketui.vo;

import lombok.Data;

@Data
public class ContainerVo {
    private String id;
    private String name;
    private String imageName;
    private String state;
    private String ip;
    private String networkMode;
    private String networkId;
}
