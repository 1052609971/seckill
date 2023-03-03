package com.example.miaosha.mq;

import com.example.miaosha.bean.SeckillMessage;
import com.example.miaosha.utils.JsonUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
* @Description: 秒杀mq生产者
        * @Author: longjian
        * @Date: 10:35 2022/6/16
        */
public class SecKillSyncProducer {
    public static void rankSecOrder(SeckillMessage message, MQProducer producer) throws Exception {
        producer.send(new Message("secOrder", "unDo", JsonUtil.object2JsonStr(message).getBytes(StandardCharsets.UTF_8)));
        System.out.println("消息已发送");
    }
}
