package com.example.miaosha.vo;

import com.example.miaosha.bean.User;
import lombok.*;

/**
* @Description: 秒杀商品详情返回对象，用于处理静态化前端时返回前端的动态数据
        * @Author: longjian
        * @Date: 21:59 2022/6/14
        */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DetailVo {
    private User user;
    private GoodsVo goodsVo;
    private int secKillStatus;
    private int remainSeconds;
}
