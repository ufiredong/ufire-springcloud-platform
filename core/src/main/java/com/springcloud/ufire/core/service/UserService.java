package com.springcloud.ufire.core.service;

import com.springcloud.ufire.core.mapper.UserMapper;
import com.springcloud.ufire.core.model.User;
import com.springcloud.ufire.core.model.UserExample;
import mboog.support.service.ReadService;
import mboog.support.service.UpsertService;
import mboog.support.service.WriteService;

public interface UserService extends ReadService<Integer, User, UserExample, UserMapper>, WriteService<Integer, User, UserExample, UserMapper>, UpsertService<Integer, User, UserExample, UserMapper> {
}