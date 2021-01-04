package com.springcloud.ufire.conf;

import com.springcloud.ufire.filter.CustomLoadBalancerClientFilter;
import com.springcloud.ufire.rule.IChooseRule;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2021-01-04 16:58
 **/
@Configuration
public class GetawayConfig {
    @Bean
    public CustomLoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client,
                                                                   LoadBalancerProperties properties,
                                                                   DiscoveryClient discoveryClient,
                                                                   List<IChooseRule> chooseRules) {
        return new CustomLoadBalancerClientFilter(client, properties,discoveryClient,chooseRules);
    }
}
