package com.springcloud.ufire.getway;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan({"com.springcloud.ufire.core.mapper"})
@SpringBootApplication
@EnableDiscoveryClient
public class UfireGetwayApplication {
    public static void main(String[] args) {
        SpringApplication.run(UfireGetwayApplication.class);
    }
}
