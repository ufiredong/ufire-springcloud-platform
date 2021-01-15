package com.springcloud.ufire.getway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class UfireGetwayApplication {
    public static void main(String[] args) {
        SpringApplication.run(UfireGetwayApplication.class);
    }
}
