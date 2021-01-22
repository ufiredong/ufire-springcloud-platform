package com.springcloud.ufire.getway.producer;

import com.springcloud.ufire.core.mapper.MessageLogMapper;
import com.springcloud.ufire.core.model.MessageLog;
import com.springcloud.ufire.core.model.ResetUser;
import com.springcloud.ufire.getway.constant.RabbitConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDateTime;
@Component
@Slf4j
public class RabbitResetUserSender {
    //自动注入RabbitTemplate模板类
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MessageLogMapper messageLogMapper;
    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData: " + correlationData);
            String messageId = correlationData.getId();
            if(ack){
                MessageLog messageLog=new MessageLog();
                //如果confirm返回成功 则进行更新
                messageLog.setMessageId(messageId);
                messageLog.setStatus(RabbitConstants.SEND_SUCCESS);
                messageLog.setUpdateTime(LocalDateTime.now());
                messageLogMapper.updateByPrimaryKey(messageLog);
            } else {
                //失败则进行具体的后续操作:重试 或者补偿等手段
                log.info("发生异常,消息没有成功发送到rabbit端");
            }
        }
    };

    //发送消息方法调用: 构建自定义对象消息
    public void send(ResetUser resetUser) throws Exception {
        // 通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //消息唯一ID
        CorrelationData correlationData = new CorrelationData(resetUser.getMessageId());
        rabbitTemplate.convertAndSend("resetUser-exchange", "resetUser.ABC", resetUser, correlationData);
    }
}
