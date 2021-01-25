package com.springcloud.ufire.getway.controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.config.impl.ServerListManager;
import com.alibaba.nacos.client.naming.NacosNamingMaintainService;
import com.alibaba.nacos.client.naming.NacosNamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-31 09:20
 **/
@RestController
@RefreshScope
@RequestMapping(value = "/getway")
public class CheckVersionController {

    @Value("${app.version:''}")
    private String version;
    @Autowired
    private NamingService namingService;

    @RequestMapping(value = "/version")
    public String getVersion() {
        return "version:" + version;
    }

    @RequestMapping(value = "/service")
    public String getService() throws NacosException {
        List<Instance> allInstances = namingService.getAllInstances("ufire-github");
        return "version:" + allInstances;
    }


}
