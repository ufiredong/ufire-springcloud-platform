//package com.websocket.ufire.config;
//
//import com.alibaba.nacos.api.exception.NacosException;
//import com.alibaba.nacos.api.naming.NamingFactory;
//import com.alibaba.nacos.api.naming.NamingService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.context.event.ApplicationStartedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @program: ufire-springcloud-platform
// * @description: X
// * @author: fengandong
// * @create: 2020-12-28 14:54
// **/
//@Configuration
//public class WebScoketInit implements CommandLineRunner {
//    @Value("${app.name}")
//    private String name;
//    @Value("${server.port}")
//    private String port;
//
//    @Override
//    public void run(String... args) throws Exception {
//        init();
//    }
//
//    public void init() {
//        try {
//            //获取nacos服务
//            NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");
//            //将服务注册到注册中心
//            namingService.registerInstance(name, "127.0.0.1", Integer.valueOf(port));
//        } catch (NacosException e) {
////            log.error("注册nacos失败", e);
//            System.out.println("注册nacos失败");
//        }
//    }
//}
