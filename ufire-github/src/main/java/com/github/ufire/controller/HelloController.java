package com.github.ufire.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-28 15:33
 **/
@RestController
@RequestMapping(value = "/hello")
public class HelloController {
    @RequestMapping(value = "/info")
    public String sayHello(){
        return "666666";
    }
}
