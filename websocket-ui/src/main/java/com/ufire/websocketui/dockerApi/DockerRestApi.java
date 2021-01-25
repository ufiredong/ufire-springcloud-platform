package com.ufire.websocketui.dockerApi;


import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class DockerRestApi {
    @Autowired
    private RestTemplate restTemplate;

    private String url="http://8.136.110.11:6066/";

    public  void  dockerPs(){
        ResponseEntity<String> result = restTemplate.getForEntity(url+"containers/json", String.class);
        JSONArray jsonArray = JSONArray.parseArray(result.toString());
        System.out.println(jsonArray);
    }
}
