package com.springcloud.ufire.getway.conf;

import com.springcloud.ufire.getway.service.RedisReceiver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            MessageListenerAdapter listenerAdapter2)
    {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        //可以添加多个 messageListener
        //可以对 messageListener对应的适配器listenerAdapter  指定本适配器 适配的消息类型  是什么
        //在发布的地方 对应发布的redisTemplate.convertAndSend("user",msg);  那这边的就对应的可以消费到指定类型的 订阅消息
        container.addMessageListener(listenerAdapter, new PatternTopic("ufire-websocket"));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RedisReceiver redisReceiver) {
        System.out.println("消息适配器1进来了");
        return new MessageListenerAdapter(redisReceiver, "receiveMessage");
    }



    //使用默认的工厂初始化redis操作模板
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }


    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        RedisSerializer keySerializer = new StringRedisSerializer();
//        RedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        //key采用字符串反序列化对象
        redisTemplate.setKeySerializer(keySerializer);
        //value也采用字符串反序列化对象
        //原因：管道操作，是对redis命令的批量操作，各个命令返回结果可能类型不同
        //可能是 Boolean类型 可能是String类型 可能是byte[]类型 因此统一将结果按照String处理
        redisTemplate.setValueSerializer(keySerializer);
        return redisTemplate;
    }

}
