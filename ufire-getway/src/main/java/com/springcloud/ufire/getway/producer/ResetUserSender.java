package com.springcloud.ufire.getway.producer;

import com.springcloud.ufire.core.model.ResetUser;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResetUserSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(ResetUser resetUser) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(resetUser.getMessageId());
        rabbitTemplate.convertAndSend("resetUser-exchange", "resetUser.abcd", resetUser, correlationData);
        /**
         * 还要在 rabbitmq 控制台配置exchange和queue，并绑定
         * 加绑定在控制台的exchange和queues哪一块都可以
         */
    }
}
