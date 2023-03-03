package com.example.miaosha.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
* @Description: 随着应用的关闭，关闭消费者和生产者
        * @Param:
        * @return:
        * @Author: longjian
        * @Date:22:25 2022/6/17
        */

@Component
public class ShutConsumerAndProducer implements DisposableBean {
    @Autowired
    private MQProducer producer;
    private DefaultMQPushConsumer consumer;
    @Override
    public void destroy() throws Exception {
        //应用关闭时关闭生产者
        producer.shutdown();
        System.out.println("生产者已关闭");
        consumer.shutdown();
        System.out.println("消费者已关闭");
    }
}
