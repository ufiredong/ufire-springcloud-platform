package com.springcloud.ufire.getway.service;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.springcloud.ufire.core.constant.Constants;
import com.springcloud.ufire.core.model.ResetUser;
import com.springcloud.ufire.getway.conf.HashRingConfig;
import com.springcloud.ufire.getway.utils.HashRingUtil;
import com.springcloud.ufire.getway.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class RedisReceiver {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HashRingConfig hashRingConfig;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ResetUserService resetUserService;

    /**
     * 监听到ufire-websocket 频道的消息,将本次获取到的实例serverMap增加虚拟节点更新hashConfig对象;
     * 将上次节点的服务节点集合更新到lastTimeServerMap;
     * 计算需要重置的user通过resetUserService.resetUserSend 通知对应的ufire-websocket 服务实例  执行session.close() 方法断开连接
     * web页面重新连接,重新路由到匹配的节点
     */
    public void receiveMessage(String message) throws Exception {
        List<Instance> list = JsonUtils.toInstances(message);
        log.info("监听到ufire-websocket服务实例上下线:{}", list);
        hashRingConfig.setLastTimeInstances(hashRingConfig.getInstances());
        List<ServiceInstance> instances = discoveryClient.getInstances(Constants.UFIRE_WEBSOCKET_REDIS_KEY);
        hashRingConfig.setInstances(instances);
        Map userMap = redisTemplate.opsForHash().entries(Constants.USER_REDIS_KEY);

        for(Instance instance:list){
            String host=instance.getIp()+instance.getPort();
            int hash = HashRingUtil.getHash(host);
        }
        Map serverMap = redisTemplate.opsForHash().entries(Constants.UFIRE_WEBSOCKET_REDIS_KEY);
        hashRingConfig.updateHashRing(serverMap, userMap);
        log.info("本次节点变动-虚拟节点插入完毕 {} ", hashRingConfig.getHashRing().getServerMap());
        log.info("上次节点变动 {} ", hashRingConfig.getHashRing().getLastTimeServerMap());
        List<ResetUser> resetUserList = hashRingConfig.getResetUserList();
        if (Objects.nonNull(resetUserList)) {
            resetUserService.resetUserSend(resetUserList);
        }
    }
}
