package com.example.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.miaosha.bean.Order;
import com.example.miaosha.bean.User;
import com.example.miaosha.vo.GoodsVo;
import com.example.miaosha.vo.OrderDetailVo;

public interface IOrderService extends IService<Order> {
    Order secKill(User user, GoodsVo goodsVo);
}
