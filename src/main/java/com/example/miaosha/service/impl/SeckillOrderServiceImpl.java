package com.example.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.miaosha.bean.SeckillOrder;
import com.example.miaosha.bean.User;
import com.example.miaosha.mapper.SeckillOrderMapper;
import com.example.miaosha.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /** 
    * @Description: 获取秒杀结果，返回订单号即成功，-1即抢购失败，0表示还在排队
            * @Param: [user, goodsId]
            * @return: java.lang.Long
            * @Author: longjian
            * @Date:16:22 2022/6/16
            */
    
    @Override
    public Long getDetail(User user, Long goodsId) {
        SeckillOrder secOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        //已经存在订单了，返回订单号代表抢购成功
        if(null!=secOrder){
            return secOrder.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty:" + goodsId)){
            return -1L;
        }else{
            return 0L;
        }
    }
}
