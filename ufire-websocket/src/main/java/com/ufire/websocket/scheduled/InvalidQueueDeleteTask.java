//package com.ufire.websocket.scheduled;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * 1、定时任务删除当前不存在的因为下线或者宕机websocket实例对应的queue
// */
//@Component
//@Slf4j
//public class InvalidQueueDeleteTask {
//
//    @Autowired
//    private RabbitAdmin rabbitAdmin;
//    @Autowired
//    private DiscoveryClient discoveryClient;
//
//    private final String SERVICE_ID="ufire-websocket";
//
//    @Scheduled(cron = "0/5 * * * * ? ") // 间隔5秒执行
//    public void deleteQueue() {
//
//        List<ServiceInstance> instances = discoveryClient.getInstances(SERVICE_ID);
//
//        rabbitAdmin.getRabbitTemplate().g
//
//}
