package com.ufire.websocketui.controller;

import com.ufire.websocketui.dockerApi.DockerRestApi;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private DockerRestApi dockerRestApi;

    @GetMapping("/index")
    public String inedx() {
        dockerRestApi.dockerPs();
        return "index";
    }

    @GetMapping("/client")

    public String client(Model model, String userId) {
        model.addAttribute("userId",userId);
        return "client";
    }




}
