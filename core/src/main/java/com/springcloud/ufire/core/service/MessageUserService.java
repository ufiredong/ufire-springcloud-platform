package com.springcloud.ufire.core.service;

import com.springcloud.ufire.core.mapper.MessageUserMapper;
import com.springcloud.ufire.core.model.MessageUser;
import com.springcloud.ufire.core.model.MessageUserExample;
import mboog.support.service.ReadService;
import mboog.support.service.UpsertService;
import mboog.support.service.WriteService;

public interface MessageUserService extends ReadService<Integer, MessageUser, MessageUserExample, MessageUserMapper>, WriteService<Integer, MessageUser, MessageUserExample, MessageUserMapper>, UpsertService<Integer, MessageUser, MessageUserExample, MessageUserMapper> {
}