package com.example.miaosha.vo;

import com.example.miaosha.bean.Goods;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;

/**
* @Description: 商品返回对象
        * @Author: longjian
        * @Date:15:23 2022/6/3
        */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GoodsVo extends Goods {
    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
