package com.example.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.miaosha.bean.SeckillOrder;
import com.example.miaosha.bean.User;

/**
 * <p>
 *  服务类
 * </p>
 *

 *
 * @author longjian
 *
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {
    Long getDetail(User user, Long goodsId);
}
