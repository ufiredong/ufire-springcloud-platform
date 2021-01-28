package com.ufire.websocket.conf;

import com.alibaba.fastjson.JSON;
import com.springcloud.ufire.core.model.ResetUser;
import com.ufire.websocket.server.MessageVo;
import com.ufire.websocket.server.MyWebSocket;
import com.ufire.websocket.util.LocalDateTimeUtils;
import com.ufire.websocket.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
@Slf4j
public class MqConfig {
    public static final String EXCHANGE = "resetUser-exchange"; // 交换空间名称

    public static final String QUEUE_NAME = "resetUser.queue" + UUID.randomUUID().toString();

    @Autowired
    private MyWebSocket myWebSocket;

    @Bean
    public Queue queue() { //
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public DirectExchange getDirectExchange() { // 使用直连的模式
        return new DirectExchange(EXCHANGE, true, true);
    }

    @Bean
    public Binding bindingExchangeQueue(DirectExchange exchange, Queue queue) {
        HostEntiyConfig myhost = (HostEntiyConfig) SpringUtil.getBean("myhost");
        return BindingBuilder.bind(queue).to(exchange).with(myhost.getIp() + ":" + myhost.getPort());
    }

    @RabbitListener(queues = "#{queue.name}")
    public void receiveMessage(ResetUser resetUser) {    // 通知 客户端 关闭 链接后重新 连接
        MessageVo messageVo = new MessageVo();
        messageVo.setType(2);
        messageVo.setDateTime(LocalDateTimeUtils.format(LocalDateTime.now()));
        messageVo.setContent("close");
        myWebSocket.sendInfo(resetUser.getUserId(), JSON.toJSONString(messageVo));
    }
}
