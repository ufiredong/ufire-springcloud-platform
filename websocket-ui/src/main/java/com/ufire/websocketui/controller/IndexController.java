package com.ufire.websocketui.controller;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-31 11:45
 **/
@Controller
@RequestMapping(value = "/websocket")
public class IndexController {
    private static AtomicInteger userId = new AtomicInteger();
    private static Map<HttpServletRequest, String> requestSessionPools = new HashMap<>();

    @GetMapping("/index")
    public String inedx() {
//        userId = new AtomicInteger();
        return "index";
    }

    @GetMapping("/client")

    public String client(Model model, String userId) {
        model.addAttribute("userId",userId);
        return "client";
    }

    public String setUserId() {


        return "";
    }

    public static void addUserId() {
        userId.incrementAndGet();
    }


}
