package com.springcloud.ufire.getway.service;

import com.springcloud.ufire.core.mapper.MessageLogMapper;
import com.springcloud.ufire.core.mapper.ResetUserMapper;
import com.springcloud.ufire.core.model.MessageLog;
import com.springcloud.ufire.core.model.ResetUser;
import com.springcloud.ufire.core.util.FastJsonConvertUtil;
import com.springcloud.ufire.getway.producer.RabbitResetUserSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResetUserService {

    @Resource
    private ResetUserMapper resetUserMapper;

    @Resource
    private MessageLogMapper messageLogMapper;

    @Autowired
    private RabbitResetUserSender rabbitResetUserSender;


    public void resetUserSend(List<ResetUser> resetUsers) throws Exception {

        for (ResetUser resetUser : resetUsers) {
            resetUserMapper.insert(resetUser);
            // 插入消息记录表数据
            MessageLog messageLog = new MessageLog();
            // 消息唯一ID
            messageLog.setMessageId(resetUser.getMessageId());
            // 保存消息整体 转为JSON 格式存储入库
            messageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(resetUser));
            // 设置消息状态为0 表示发送中
            messageLog.setStatus("0");
            // 设置消息未确认超时时间窗口为 一分钟
            messageLog.setNextRetry(LocalDateTime.now());
            messageLog.setCreateTime(LocalDateTime.now());
            messageLog.setUpdateTime(LocalDateTime.now());
            messageLogMapper.insertSelective(messageLog);
            // 发送消息
            rabbitResetUserSender.sendOrder(resetUser);
        }

    }

}
