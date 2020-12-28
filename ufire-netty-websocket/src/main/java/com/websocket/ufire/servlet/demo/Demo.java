package com.websocket.ufire.servlet.demo;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;

import javax.servlet.annotation.WebServlet;

/**
 * @program: ufire-springcloud-platform
 * @description: X
 * @author: fengandong
 * @create: 2020-12-28 16:20
 **/
@WebServlet(loadOnStartup=1,urlPatterns={"/hello"})
public class Demo  extends WebMvcProperties.Servlet {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse){
        System.out.println("00000000000000000");
    }
}
