//package com.springcloud.ufire.rule;
//
//import com.alibaba.nacos.client.naming.utils.RandomUtils;
//import com.netflix.client.config.IClientConfig;
//import com.netflix.loadbalancer.AbstractLoadBalancerRule;
//import com.netflix.loadbalancer.Server;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @program: ufire-springcloud-platform
// * @description: X
// * @author: fengandong
// * @create: 2021-01-04 15:37
// **/
//@Component
//public class CustomerLoadBalanceRule extends AbstractLoadBalancerRule {
//    private static final Log log = LogFactory.getLog(CustomerLoadBalanceRule.class);
//    @Override
//    public void initWithNiwsConfig(IClientConfig iClientConfig) {
//
//    }
//
//    @Override
//    public Server choose(Object o) {
//        log.info("key is " + o);
//        List<Server> servers = this.getLoadBalancer().getReachableServers();
//        log.info("servers " + servers);
//        if (servers.isEmpty()) {
//            return null;
//        }
//        if (servers.size() == 1) {
//            return servers.get(0);
//        }
//        return randomChoose(servers);
//    }
//
//    /**
//     *
//     * <p>随机返回一个服务实例 </p>
//     * @param servers 服务列表
//     */
//    private Server randomChoose(List<Server> servers) {
//        int randomIndex = RandomUtils.nextInt(servers.size());
//        return servers.get(randomIndex);
//    }
//
//
//}
