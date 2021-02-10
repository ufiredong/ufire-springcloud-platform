package com.springcloud.ufire.getway.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisReceiver {
    public void receiveMessage(String message) {
        log.info("监听到ufire-websocket实例上下线:{}",message);
        JSONArray jsonArray = JSONArray.parseArray(message);
    }
}
