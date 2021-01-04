package com.springcloud.ufire.filter;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.springcloud.ufire.rule.ConsistencyChooseRule;
import com.springcloud.ufire.rule.IChooseRule;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2021-01-04 16:48
 **/
public class CustomLoadBalancerClientFilter extends LoadBalancerClientFilter implements BeanPostProcessor {
    private final DiscoveryClient discoveryClient;

    private final List<IChooseRule> chooseRules;
    public CustomLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties, DiscoveryClient discoveryClient, List<IChooseRule> chooseRules) {
        super(loadBalancer, properties);
        this.discoveryClient = discoveryClient;
        this.chooseRules = chooseRules;
        chooseRules.add(new ConsistencyChooseRule());
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        if(!CollectionUtils.isEmpty(chooseRules)){
            Iterator<IChooseRule> iChooseRuleIterator = chooseRules.iterator();
            while (iChooseRuleIterator.hasNext()){
                IChooseRule chooseRule = iChooseRuleIterator.next();
                ServiceInstance choose = chooseRule.choose(exchange,discoveryClient);
                if(choose != null){
                    return choose;
                }
            }
        }
        return loadBalancer.choose(
                ((URI) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR)).getHost());
    }
}
