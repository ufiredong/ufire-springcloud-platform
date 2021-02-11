package com.springcloud.ufire.getway.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: fengandong
 * @time: 2021/2/11 13:11
 */
public class JsonUtils {


    public static List<Instance> toInstances(String message) {
        List<Instance> list = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(message);
        jsonArray.stream().forEach(json -> {
            JSONObject instanceJson = JSONObject.parseObject(json.toString());
            Instance instance = JSON.toJavaObject(instanceJson, Instance.class);
            list.add(instance);
        });
        return list;
    }
}
