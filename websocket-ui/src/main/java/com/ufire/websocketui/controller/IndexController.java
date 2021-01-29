package com.ufire.websocketui.controller;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.ufire.websocketui.utils.DockerClientUtil;
import com.ufire.websocketui.utils.LocalDateTimeUtils;
import com.ufire.websocketui.vo.ContainerVo;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

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
    private DockerClient dockerClient;
    @Autowired
    private DockerClientUtil dockerClientUtil;
    @Value("${app.wssUrl}")
    private String wssUrl;
    @Value("${app.messageUrl}")
    private String messageUrl;
//    @Autowired
//    private WebSocketApi webSocketApi;

    @GetMapping("/index")
    public String inedx() {
        return "index";
    }

    @GetMapping("/client")
    public String client(Model model, String userId) {
        model.addAttribute("userId", userId);
        model.addAttribute("wssUrl", wssUrl);
        model.addAttribute("messageUrl", messageUrl);
        return "client";
    }

    @GetMapping("/containerList")
    public String list(@Autowired Model model, @RequestParam(required = false) String operat, @RequestParam(required = false) String id) {
        if (Objects.nonNull(operat)) {
            if (operat.equals("1")) {
                dockerClientUtil.startContainer(dockerClient, id);
            }
            if (operat.equals("2")) {
                dockerClientUtil.stopContainer(dockerClient, id);
            }
            if (operat.equals("3")) {
                dockerClientUtil.removeContainer(dockerClient, id);
            }
            if (operat.equals("4")) {
                dockerClientUtil.createContainers(dockerClient, "ufire-websocket-" + (int) ((Math.random() * 9 + 1) * 100000), "ufire-websocket");
            }
        }
        getList(model);
        return "index::div1";
    }

    private void getList(@Autowired Model model) {
        List<ContainerVo> containerVoList = new ArrayList<>();
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        listContainersCmd.withShowAll(true).getFilters().put("name", Arrays.asList("ufire-websocket"));
        List<Container> containers = listContainersCmd.exec();
        containers.stream().filter(container -> !container.getImage().equals("ufire-websocket-ui")).forEach(container -> {
            ContainerVo containerVo = new ContainerVo();
            String id = container.getId().substring(0, 6);
            containerVo.setId(id);
            containerVo.setStatus(container.getStatus());
            containerVo.setIp(container.getNetworkSettings().getNetworks().get("ufire-springcloud-platform_default").getIpAddress());
            containerVo.setPort("80");
            containerVo.setName(Arrays.asList(container.getNames()).get(0));
            containerVo.setCreatTime(LocalDateTimeUtils.toLocalDateTime(container.getCreated()));
            containerVo.setStatus(container.getStatus());

            containerVo.setState(container.getState());
            containerVoList.add(containerVo);
        });
        model.addAttribute("containerVoList", containerVoList);
    }
}
