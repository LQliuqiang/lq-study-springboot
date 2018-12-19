package lq.study.springboot.rabbit;

import com.rabbitmq.client.Channel;
import lq.study.springboot.bean.UserInfo;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class RabbitReceiver {


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "user-info-queue",
                    durable = "true"),//durable持久化
            exchange = @Exchange(value = "userInfo-exchange",
                    durable = "true",
                    type = "topic",//一topic接收形式
                    ignoreDeclarationExceptions = "true"),
            key = "userInfo.*" //接收路由key以userInfo.开头的所有路由消息
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        System.err.println("--------------------------------------");
        String s = new String((byte[]) message.getPayload(), StandardCharsets.UTF_8);
        System.err.println("RabbitMQ消费端Payload: " +s);
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手工ACK
        channel.basicAck(deliveryTag, false);
    }


}
