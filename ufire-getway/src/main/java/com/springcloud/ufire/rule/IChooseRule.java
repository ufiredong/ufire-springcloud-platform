package com.springcloud.ufire.rule;

import com.springcloud.ufire.conf.HashRingConfig;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.server.ServerWebExchange;

public interface IChooseRule {
    /**
     * 返回null那么使用gateway默认的负载均衡策略
     * @param exchange
     * @param hashRingConfig
     * @return
     */
    ServiceInstance choose(ServerWebExchange exchange, HashRingConfig hashRingConfig);
}
