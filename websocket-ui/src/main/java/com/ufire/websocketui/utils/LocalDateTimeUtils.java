package com.ufire.websocketui.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @description:
 * @author: fengandong
 * @time: 2021/1/21 23:14
 */
public class LocalDateTimeUtils {
    public static LocalDateTime toLocalDateTime(Long time){
        Instant instant = Instant.ofEpochMilli(time);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
}
