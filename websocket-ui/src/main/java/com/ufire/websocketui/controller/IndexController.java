package com.ufire.websocketui.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import com.ufire.websocketui.utils.DockerClientUtil;
import com.ufire.websocketui.utils.LocalDateTimeUtils;
import com.ufire.websocketui.vo.ContainerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private  DockerClient dockerClient;
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
    @GetMapping("/containerList")
    public String list(@Autowired Model model, @RequestParam(required = false) String operat,@RequestParam(required = false) String iid){
        List<ContainerVo> containerVoList=new ArrayList<>();
        List<Container> exec = dockerClient.listContainersCmd().exec();
        List<Container> websocket = exec.stream().filter(container -> container.getImage().equals("ufire-websocket")).collect(Collectors.toList());
            websocket.stream().forEach(container->{
            ContainerVo containerVo = new ContainerVo();
            String id=container.getId().substring(0,6);
            containerVo.setId(id);
            containerVo.setStatus(container.getStatus());

            containerVo.setIp(container.getNetworkSettings().getNetworks().get("ufire-springcloud-platform_default").getIpAddress());
            containerVo.setPort("80");
            containerVo.setName(Arrays.asList(container.getNames()).get(0));
            containerVo.setCreatTime(LocalDateTimeUtils.toLocalDateTime(container.getCreated()));
            containerVo.setStatus(container.getStatus());
            containerVo.setState(container.getState());
            containerVo.setOperat("containerList?operat=start&id="+id);
            containerVoList.add(containerVo);
        });
        model.addAttribute("containerVoList",containerVoList);
        return "index::div1";
    }
//    @GetMapping("{operat}/{id}")
//    public String list(@Autowired Model model, @PathVariable("operat") String operat,@PathVariable("id") String oid){
//        List<ContainerVo> containerVoList=new ArrayList<>();
//        List<Container> exec = dockerClient.listContainersCmd().exec();
//        List<Container> websocket = exec.stream().filter(container -> container.getImage().equals("ufire-websocket")).collect(Collectors.toList());
//        websocket.stream().forEach(container->{
//            ContainerVo containerVo = new ContainerVo();
//            String id=container.getId().substring(0,6);
//            containerVo.setId(id);
//            containerVo.setStatus(container.getStatus());
//            containerVo.setIp("192.168.1.1");
//            containerVo.setPort("80");
//            containerVo.setName(Arrays.asList(container.getNames()).get(0));
//            containerVo.setCreatTime(LocalDateTimeUtils.toLocalDateTime(container.getCreated()));
//            containerVo.setStatus(container.getStatus());
//            containerVo.setOperat("start/"+id);
//            containerVoList.add(containerVo);
//        });
//        model.addAttribute("containerVoList",containerVoList);
//        return "index::div1";
//    }



}
