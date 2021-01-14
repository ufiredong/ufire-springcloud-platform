package com.springcloud.ufire.conf;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {
    private Logger logger = LoggerFactory.getLogger(ThreadPoolConfig.class);
    /** 获取当前系统的CPU 数目*/
    int cpuNums = Runtime.getRuntime().availableProcessors();
    /** 线程池核心池的大小*/
    private  int corePoolSize = 10;
    /** 线程池的最大线程数*/
    private  int maximumPoolSize = cpuNums * 5;

    /**
     * 消费队列线程
     * @return
     */
    @Bean
    public ExecutorService buildThreadPool(){
        logger.info("TreadPoolConfig创建线程数:"+corePoolSize+",最大线程数:"+maximumPoolSize);
        ExecutorService pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100),new ThreadFactoryBuilder().setNameFormat("GetwayThread-%d").build());

        return pool ;
    }
}
