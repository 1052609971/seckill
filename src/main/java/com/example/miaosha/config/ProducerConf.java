package com.example.miaosha.config;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/** 
* @Description: 配置mq生产者，注入到ioc容器里
        * @Param: 
        * @return: 
        * @Author: longjian
        * @Date:22:48 2022/6/17
        */

@Configuration
public class ProducerConf {
    @Bean
    public MQProducer getProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("SecKillProducer");
        producer.setNamesrvAddr("122.51.174.149:9876");
        //设置失败重试次数
        producer.setRetryTimesWhenSendAsyncFailed(3);
        //开启生产者
        producer.start();
        System.out.println("生产者已开启");
        return producer;
    }
}
