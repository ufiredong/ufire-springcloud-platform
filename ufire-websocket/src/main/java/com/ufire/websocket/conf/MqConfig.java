package com.ufire.websocket.conf;
import com.springcloud.ufire.core.model.ResetUser;
import com.ufire.websocket.server.MyWebSocket;
import com.ufire.websocket.util.IPutil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.websocket.Session;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
@Configuration
@Slf4j
public class MqConfig {
    public static final String EXCHANGE = "resetUser-exchange"; // 交换空间名称
    Map<String, Session> sessionPools = MyWebSocket.sessionPools;
    public static String QUEUE_NAME;

    static {
        try {
            QUEUE_NAME = "resetUser.queue" + IPutil.getIp();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private MyWebSocket myWebSocket;

    @Bean
    public Queue queue() { //
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public DirectExchange getDirectExchange() { // 使用直连的模式
        return new DirectExchange(EXCHANGE, true, true);
    }

    @Bean
    public Binding bindingExchangeQueue(DirectExchange exchange, Queue queue) throws UnknownHostException {
        return BindingBuilder.bind(queue).to(exchange).with(IPutil.getIp() + ":" + 80);
    }

    /**
     * 监听到本服务的需要重置的user消息，从sessionPools获取到session
     * 执行session.close()方法主动断开与websocket客户端的链接
     *
     * @param resetUser
     */
    @RabbitListener(queues = "#{queue.name}")
    public void receiveMessage(ResetUser resetUser) {
        try {
            Session session = sessionPools.get(resetUser.getUserId());
            session.close();
        } catch (IOException e) {
            log.error("receiveMessage 发生异常:{}", e.getMessage());
        }
    }
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

}
