package com.ufire.websocket.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @description:
 * @author: fengandong
 * @time: 2021/1/28 21:48
 */
public class IPutil {
    public static String getIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
