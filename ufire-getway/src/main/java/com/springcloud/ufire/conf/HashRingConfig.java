package com.springcloud.ufire.conf;

import com.springcloud.ufire.entiy.HashRingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;

import java.util.*;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2021-01-05 11:11
 **/
public class HashRingConfig {

    private static final Logger log = LoggerFactory.getLogger(HashRingConfig.class);
    //真实结点需要对应的虚拟节点个数
    private static final int VIRTUAL_NODES = 100;

    private HashRingEntity hashRing;

    private List<ServiceInstance> instances;

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

    public void setInstances(List<ServiceInstance> instances) {
        this.instances = instances;
    }


    /**
     * 计算userId的hash值求得需要路由到的节点
     */
    public ServiceInstance getServer(String UserId) {
        int userHash = getHash(UserId);
        SortedMap<Integer, String> serverMap = hashRing.getServerMap();
        // 遍历 有序Map serverHash从小到大  如果 serverHash  大于 userHash  则被视为第一个大于userHash的 hash值,
        // 第一个大于userHash 的 节点hash 就是需要路由到的节点如果是虚拟节点需解析获得真实节点。
        for (Integer serverHash : serverMap.keySet()) {
            if (serverHash > userHash) {
                ServiceInstance instance = getServiceInstance(serverMap, serverHash);
                return instance;
            }
        }
        // 遍历完发现 userHash最大, 则路由到 serverhash节点环的第一个节点。
        Integer serverHash = serverMap.firstKey();
        ServiceInstance instance = getServiceInstance(serverMap, serverHash);
        return instance;
    }

    /**
     * 解析虚拟节点对应的真实节点，匹配对应的 instance 返回
     */
    private ServiceInstance getServiceInstance(SortedMap<Integer, String> serverMap, Integer serverHash) {
        String server = serverMap.get(serverHash);
        if (server.indexOf(("&&")) != -1) {
            server = server.substring(0, server.indexOf("&&"));
        }
        String host = server.substring(0, server.indexOf(":"));
        String port = server.substring(server.indexOf(":") + 1, server.length());
        for (ServiceInstance instance : instances) {
            if (instance.getHost().equals(host) && instance.getPort() == Integer.parseInt(port)) {
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
}
