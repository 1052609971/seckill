package com.example.miaosha.config;

import com.example.miaosha.bean.Order;
import com.example.miaosha.bean.SeckillMessage;
import com.example.miaosha.bean.User;
import com.example.miaosha.service.impl.GoodsServiceImpl;
import com.example.miaosha.service.impl.OrderServiceImpl;
import com.example.miaosha.utils.JsonUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
* @Description: 配置MQ消费者并且开启
        * @Author: longjian
        * @Date: 22:17 2022/6/17
        */

@Configuration
public class ConsumerConf {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsServiceImpl goodsService;
    @Bean
    public DefaultMQPushConsumer getConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("SecKillConsumer");
        consumer.setNamesrvAddr("122.51.174.149:9876");
        //订阅秒杀服务主题下所有的tag
        consumer.subscribe("secOrder", "*");
        //从当前queue中第一个开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //创建消费者监听器，与rocketmq的broker建立长连接
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (Message message :msgs) {
                String orderString =  new String(message.getBody());
                SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(orderString, SeckillMessage.class);
                User user = seckillMessage.getUser();
                //判断库存
                if(goodsService.findGoodsVoByGoodsId(seckillMessage.getGoodId()).getStockCount()>0){
                    //创建订单
                    Order order = orderService.secKill(seckillMessage.getUser(), goodsService.findGoodsVoByGoodsId(seckillMessage.getGoodId()));
                    //订单消息存入同步更新到redis里面
                    redisTemplate.opsForValue().set("order:"+user.getId()+":"+seckillMessage.getGoodId(), order);
                }
            }
            //订单消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.println("消费者已开启");
        return consumer;
    }
}
