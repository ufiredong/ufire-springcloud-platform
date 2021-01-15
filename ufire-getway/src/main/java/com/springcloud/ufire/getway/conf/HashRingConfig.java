package com.springcloud.ufire.getway.conf;

import com.springcloud.ufire.core.mapper.MessageLogMapper;
import com.springcloud.ufire.core.mapper.ResetUserMapper;
import com.springcloud.ufire.core.model.MessageLog;
import com.springcloud.ufire.core.model.ResetUser;
import com.springcloud.ufire.getway.entiy.HashRingEntity;
import com.springcloud.ufire.getway.producer.RabbitResetUserSender;
import com.springcloud.ufire.getway.producer.ResetUserSender;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2021-01-05 11:11
 **/
@Data
public class HashRingConfig {

    private static final Logger log = LoggerFactory.getLogger(HashRingConfig.class);
    //真实结点需要对应的虚拟节点个数
    private static final int VIRTUAL_NODES = 100;

    private HashRingEntity hashRing;

    private List<ServiceInstance> instances;

    private List<ServiceInstance> lastTimeInstances;

    /**
     * 修改hash环，将上次的节点hash值赋值给 lastTimeServerMap 保存
     */
    public void updateHashRing(Map<String, String> serverMap, Map<String, String> userMap) {
        HashRingEntity hashRingEntity = new HashRingEntity();
        SortedMap<Integer, String> serverSortedMap = new TreeMap<>();
        SortedMap<Integer, String> userSortedMap = new TreeMap<>();
        for (String key : serverMap.keySet()) {
            serverSortedMap.put(Integer.parseInt(key), serverMap.get(key));
        }
        for (String key : userMap.keySet()) {
            userSortedMap.put(Integer.parseInt(key), userMap.get(key));
        }
        hashRingEntity.setLastTimeServerMap(Objects.nonNull(hashRing) ? hashRing.getServerMap() : null);
        hashRingEntity.setServerMap(serverSortedMap);
        hashRingEntity.setUserMap(userSortedMap);
        this.hashRing = hashRingEntity;
    }

    public HashRingEntity getHashRing() {
        return this.hashRing;
    }


    /**
     * 计算userId的hash值求得需要路由到的节点
     */
    public ServiceInstance getServer(String userId) {
        int userHash = getHash(userId);
        SortedMap<Integer, String> serverMap = hashRing.getServerMap();
        // 遍历 有序Map serverHash从小到大  如果 serverHash  大于 userHash  则被视为第一个大于userHash的 hash值,
        // 第一个大于userHash 的 节点hash 就是需要路由到的节点如果是虚拟节点需解析获得真实节点。
        for (Integer serverHash : serverMap.keySet()) {
            if (serverHash > userHash) {
                log.info("用户{}: hash:{},在serverMap有序SortedMap(hash环)上距离节点 hash:{} 最近", userId, userHash, serverHash);
                ServiceInstance instance = getServiceInstance(serverMap, serverHash);
                return instance;
            }
        }
        // 遍历完发现 userHash最大, 则路由到 serverhash节点环的第一个节点。
        Integer serverHash = serverMap.firstKey();
        log.info("用户{}: hash:{},在serverMap有序SortedMap(hash环)上自己最大则指向serverMap的最小值即为第一个节点hash:{}", userId, userHash, serverHash);
        ServiceInstance instance = getServiceInstance(serverMap, serverHash);
        return instance;
    }

    /**
     * 解析虚拟节点对应的真实节点，匹配对应的 instance 返回
     */
    private ServiceInstance getServiceInstance(SortedMap<Integer, String> serverMap, Integer serverHash) {
        String node = serverMap.get(serverHash);
        String server = null;
        if (node.indexOf(("&&")) != -1) {
            server = node.substring(0, node.indexOf("&&"));
        } else {
            server = node;
        }
        String host = server.substring(0, server.indexOf(":"));
        String port = server.substring(server.indexOf(":") + 1, server.length());
        for (ServiceInstance instance : instances) {
            if (instance.getHost().equals(host) && instance.getPort() == Integer.parseInt(port)) {
                log.info("通过解析最近的节点hash:{}，得到节点:{},路由到websocket实例:{}", serverHash, node, instance.getUri());
                return instance;
            }
        }
        return null;
    }

    /**
     * 计算hash值
     */
    public int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    /**
     * 加入虚拟节点
     */
    public void addVirtualNode(HashRingEntity hashRing) {
        SortedMap<Integer, String> serverMap = hashRing.getServerMap();
        SortedMap<Integer, String> virtualServerMap = new TreeMap<>();
        for (String realNode : serverMap.values()) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNode = realNode + "&&VN" + String.valueOf(i);
                int virtualNodeHash = getHash(virtualNode);
                virtualServerMap.put(virtualNodeHash, virtualNode);
            }
        }
        serverMap.putAll(virtualServerMap);
        hashRing.setServerMap(serverMap);
        this.hashRing = hashRing;
    }

    /**
     * 增加真实节点后重置一部分user的连接
     */
    public List<ResetUser> getResetUserList() {
        if (instances.size() < lastTimeInstances.size()) {
            return null;
        }
        List<ResetUser> resetUsers = new ArrayList<>();
        List<String> nowServer = new ArrayList<>();
        List<String> lastServer = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            nowServer.add(instance.getHost() + ":" + instance.getPort());
        }
        for (ServiceInstance instance : lastTimeInstances) {
            lastServer.add(instance.getHost() + ":" + instance.getPort());
        }
        List<String> newServer = nowServer.stream().filter(server -> !lastServer.contains(server)).collect(Collectors.toList());
        SortedMap<Integer, String> userMap = hashRing.getUserMap();
        SortedMap<Integer, String> serverMap = hashRing.getServerMap();
        Iterator<Map.Entry<Integer, String>> it = userMap.entrySet().iterator();
        for (String server : newServer) {
            while (it.hasNext()) {
                Map.Entry<Integer, String> user = it.next();
                int userHash = user.getKey();
                String userId = user.getValue();
                serverMap.put(userHash, userId);
                SortedMap<Integer, String> thanUserMap = ((TreeMap<Integer, String>) serverMap).tailMap(userHash);  //大于user hash的部分map
                thanUserMap.remove(userHash, userId);
                Integer firstKey = thanUserMap.firstKey();
                String value = thanUserMap.get(firstKey);
                if (value.indexOf(server) != -1) {
                    log.info("用户{}需要重新进行链接到", userId);
                    ResetUser resetUser = new ResetUser();
                    resetUser.setMessageId(UUID.randomUUID().toString());
                    resetUser.setName("用户:" + userId);
                    resetUser.setReconnect(getServer(userId).getUri().toString());
                    resetUsers.add(resetUser);
                }
            }
        }
        return resetUsers;
    }

}
