package com.springcloud.ufire.rule;

import com.springcloud.ufire.conf.HashRingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.List;

/**
 * @program: ufire-springcloud-platform
 * @description:
 * @author: fengandong
 * @create: 2021-01-04 16:51
 **/
@Component
@Slf4j
public class ConsistencyChooseRule implements IChooseRule {

    @Override
    public ServiceInstance choose(ServerWebExchange exchange, HashRingConfig hashRingConfig) {
        URI originalUrl = (URI) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String instancesId = originalUrl.getHost();
        if (instancesId.equals("ufire-websocket")) {
            if (originalUrl.getPath().contains("/socket")) {
                try {
                    List<PathContainer.Element> elements = exchange.getRequest().getPath().elements();
                    log.info("解析转发url:{} userId:{}", exchange.getRequest().getURI(), elements.get(elements.size() - 1));
                    String userId = elements.get(elements.size() - 1).value();
                    return hashRingConfig.getServer(userId);
                } catch (Exception e) {
                    //do nothing
                }
            }
        }

        return null;
    }
}
