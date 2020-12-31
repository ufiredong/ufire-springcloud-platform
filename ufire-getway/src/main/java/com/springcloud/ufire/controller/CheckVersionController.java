package com.springcloud.ufire.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-31 09:20
 **/
@RestController
@RefreshScope
@RequestMapping(value = "/getway")
public class CheckVersionController {

    @Value("${app.version:''}")
    private String version;

    @RequestMapping(value = "/version")
    public String getVersion() {
        return "version:"+version;
    }
}
