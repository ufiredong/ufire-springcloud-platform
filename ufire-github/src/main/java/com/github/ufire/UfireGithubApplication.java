package com.github.ufire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @author: fengandong
 * @time: 2020/12/25 21:41
 */
@EnableDiscoveryClient
@SpringBootApplication
public class UfireGithubApplication {
    public static void main(String[] args) {
        SpringApplication.run(UfireGithubApplication.class);
    }
}
