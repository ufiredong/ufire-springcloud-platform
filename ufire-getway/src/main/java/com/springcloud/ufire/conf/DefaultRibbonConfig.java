//package com.springcloud.ufire.conf;
//
//import com.netflix.loadbalancer.IRule;
//import com.springcloud.ufire.filter.CustomLoadBalancerClientFilter;
//import org.springframework.cloud.netflix.ribbon.RibbonClient;
//import org.springframework.cloud.netflix.ribbon.RibbonClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @description:
// * @author: Andy
// * @time: 2020/12/31 22:50
// */
//@Configuration
//@RibbonClients(
//        value = {
//                @RibbonClient(name = "gatewayRule", configuration = CustomLoadBalancerClientFilter.class),
//        },
//        defaultConfiguration = DefaultRibbonConfig.class)
//public class DefaultRibbonConfig {
//    @Bean
//    public IRule ribbonRule() {
//        return new CustomLoadBalancerClientFilter();
//    }
//}
