package com.springcloud.ufire.core.service;

import com.springcloud.ufire.core.mapper.MessageLogMapper;
import com.springcloud.ufire.core.model.MessageLog;
import com.springcloud.ufire.core.model.MessageLogExample;
import mboog.support.service.ReadService;
import mboog.support.service.UpsertService;
import mboog.support.service.WriteService;

public interface MessageLogService extends ReadService<String, MessageLog, MessageLogExample, MessageLogMapper>, WriteService<String, MessageLog, MessageLogExample, MessageLogMapper>, UpsertService<String, MessageLog, MessageLogExample, MessageLogMapper> {
}