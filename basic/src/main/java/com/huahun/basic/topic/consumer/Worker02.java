package com.huahun.basic.topic.consumer;

import com.huahun.basic.common.constant.RabbitMqConstant;
import com.huahun.basic.common.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @ClassName Worker02
 * @Description TODO
 * @Author zzh
 * @Date 2021/8/6 14:26
 * @Version 1.0
 */
@Slf4j
public class Worker02 {
    public static void main(String[] args) {
        Channel channel = RabbitMqUtils.getChannel();
        try {
            //声明一个交换机
            channel.exchangeDeclare(RabbitMqConstant.RABBITMQ_EXCHANGE_NAME_TOPIC, BuiltinExchangeType.TOPIC);
            //声明一个随机名字的临时队列
            channel.queueDeclare(RabbitMqConstant.RABBITMQ_QUEUE_NAME_TOPIC_WOKER02, false, false, false,null);
            //绑定交换机与队列
            channel.queueBind(RabbitMqConstant.RABBITMQ_QUEUE_NAME_TOPIC_WOKER02,
                    RabbitMqConstant.RABBITMQ_EXCHANGE_NAME_TOPIC,
                    RabbitMqConstant.RABBITMQ_ROUTINGKEYS_NAME_TOPIC_WORKER02_KEY1);
            channel.queueBind(RabbitMqConstant.RABBITMQ_QUEUE_NAME_TOPIC_WOKER02,
                    RabbitMqConstant.RABBITMQ_EXCHANGE_NAME_TOPIC,
                    RabbitMqConstant.RABBITMQ_ROUTINGKEYS_NAME_TOPIC_WORKER02_KEY2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //接收成功的回调函数
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            log.info("消费者Worker02成功接收消息：" + new String(message.getBody()) + ",绑定键：" + message.getEnvelope().getRoutingKey());
        };

        //取消接收的回调函数
        CancelCallback cancelCallback = (consumerTag) -> {
            log.info("消费者Worker02接收消息被中断");
        };
        try {
            channel.basicConsume(RabbitMqConstant.RABBITMQ_QUEUE_NAME_TOPIC_WOKER02,true,deliverCallback,cancelCallback);
            log.info("消费者Worker02启动完毕，正在等待接受消息...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
