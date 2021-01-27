package com.ufire.websocket.conf;

import com.ufire.websocket.util.SpringUtil;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
    public static final String EXCHANGE = "resetUser-exchange"; // 交换空间名称
    @Bean
    public Queue queue() { //
        HostEntiyConfig myhost =(HostEntiyConfig) SpringUtil.getBean("myhost");
        return new Queue("rresetUser.queue".concat("192.168.86.137" + ":" + myhost.getPort()));
    }

    @Bean
    public DirectExchange getDirectExchange() { // 使用直连的模式
        return new DirectExchange(EXCHANGE, true, true);
    }

    @Bean
    public Binding bindingExchangeQueue(DirectExchange exchange, Queue queue) {
        HostEntiyConfig myhost =(HostEntiyConfig) SpringUtil.getBean("myhost");
        return BindingBuilder.bind(queue).to(exchange).with("192.168.86.137" + ":" + myhost.getPort());
    }
}
