package com.ufire.websocket.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-31 17:47
 **/
@Configuration
@Data
public class HostEntiyConfig {
    private String ip;
    @Value("${server.port}")
    private String port;
    @Bean(value = "myhost")
    public HostEntiyConfig getHostEntiyConfig() throws UnknownHostException {
        HostEntiyConfig hostEntiyConfig = new HostEntiyConfig();
        hostEntiyConfig.setIp(InetAddress.getLocalHost().getHostAddress());
        hostEntiyConfig.setPort(port);
        return hostEntiyConfig;
    }

    @Override
    public String toString(){
        return "『192.168.86.137:"+this.getPort() +"』";
    }

}
