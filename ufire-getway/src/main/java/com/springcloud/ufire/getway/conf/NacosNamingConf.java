package com.springcloud.ufire.getway.conf;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.client.naming.NacosNamingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-31 10:12
 **/
@Configuration
public class NacosNamingConf {

    private static NamingService namingService;

    @Bean(name="namingService")
    public NamingService get() throws NacosException {
        namingService = NacosFactory.createNamingService("127.0.0.1");
        return namingService;
    }

}
