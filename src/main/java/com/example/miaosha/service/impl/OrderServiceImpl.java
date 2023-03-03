package com.example.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.miaosha.bean.Order;
import com.example.miaosha.bean.SeckillGoods;
import com.example.miaosha.bean.SeckillOrder;
import com.example.miaosha.bean.User;
import com.example.miaosha.mapper.OrderMapper;
import com.example.miaosha.service.IOrderService;
import com.example.miaosha.service.ISeckillGoodsService;
import com.example.miaosha.service.ISeckillOrderService;
import com.example.miaosha.vo.GoodsVo;
import com.example.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service

public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService{
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Order secKill(User user, GoodsVo goodsVo) {
        SeckillGoods goods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        //减库存
        goods.setStockCount(goods.getStockCount()-1);
        //在更新的时候利用sql语句来判断库存是否还有
        //这里利用mysql的在默认隔离级别下使用曾删改会对查询字段加行锁的特性对秒杀商品进行加锁，
        // x锁是一种悲观锁能够防止超卖，mysql在增删改的情况下如果不是按照索引来查找的话会加表级锁
        seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().set("stock_count", goods.getStockCount()).eq("id", goods.getId()).gt("stock_count", 0));
        //创建订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //创建秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrderService.save(seckillOrder);
        return order;
    }
}
