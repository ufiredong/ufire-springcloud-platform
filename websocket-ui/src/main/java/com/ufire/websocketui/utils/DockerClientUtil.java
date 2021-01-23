package com.ufire.websocketui.utils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

/**
 * @description:
 * @author: fengandong
 * @time: 2021/1/21 20:46
 */
@Component
public class DockerClientUtil {
    /**
     * 连接Docker服务器
     * @return
     * @param
     */
    @Bean
    public DockerClient connectDocker(){
        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://8.136.110.11:6066").build();
        dockerClient.infoCmd().exec();
        return dockerClient;
    }

    /**
     * 创建容器
     * @param client
     * @return
     */
    public CreateContainerResponse createContainers(DockerClient client, String containerName, String imageName){

        CreateContainerResponse container = client.createContainerCmd(imageName)
                .withHostConfig(newHostConfig().withNetworkMode("ufire-springcloud-platform_default"))
                .withName(containerName).exec();
        return container;

    }


    /**
     * 启动容器
     * @param client
     * @param containerId
     */
    public void startContainer(DockerClient client,String containerId){
        client.startContainerCmd(containerId).exec();
    }

    /**
     * 停止容器
     * @param client
     * @param containerId
     */
    public void stopContainer(DockerClient client,String containerId){
        client.stopContainerCmd(containerId).exec();
    }

    /**
     * 删除容器
     * @param client
     * @param containerId
     */
    public void removeContainer(DockerClient client,String containerId){
        client.removeContainerCmd(containerId).exec();
    }
}
