package com.ufire.websocketui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: ufire-springcloud-platform
 * @description: x
 * @author: fengandong
 * @create: 2020-12-31 11:52
 **/

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class WebsocketUiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsocketUiApplication.class);
    }
}
