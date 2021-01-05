package com.springcloud.ufire.entiy;

import lombok.Data;

import java.util.Map;
import java.util.SortedMap;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2021-01-05 11:36
 **/
@Data
public class HashRingEntity {
    // 服务节点hash集合
    private SortedMap<Integer, String> serverMap;
    // 当前在线用户hash集合
    private SortedMap<Integer, String> userMap;
    // 上次服务节点hash集合
    private SortedMap<Integer, String> lastTimeServerMap;
    // 本次需要改动的用户hash映射节点hash集合
    private SortedMap<Integer, String> userMovMap;
}
