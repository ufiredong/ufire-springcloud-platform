package com.springcloud.ufire.core.service;

import com.springcloud.ufire.core.mapper.ResetUserMapper;
import com.springcloud.ufire.core.model.ResetUser;
import com.springcloud.ufire.core.model.ResetUserExample;
import mboog.support.service.ReadService;
import mboog.support.service.UpsertService;
import mboog.support.service.WriteService;

public interface ResetUserService extends ReadService<Integer, ResetUser, ResetUserExample, ResetUserMapper>, WriteService<Integer, ResetUser, ResetUserExample, ResetUserMapper>, UpsertService<Integer, ResetUser, ResetUserExample, ResetUserMapper> {
}