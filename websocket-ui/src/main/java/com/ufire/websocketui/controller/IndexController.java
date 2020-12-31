package com.ufire.websocketui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-31 11:45
 **/
@Controller
@RequestMapping(value = "/websocket")
public class IndexController {
    @GetMapping("/index")
    public String inedx() {
        return "index";
    }

    @GetMapping("/client")
    public String client(Model model) {
        return "client";
    }
}
