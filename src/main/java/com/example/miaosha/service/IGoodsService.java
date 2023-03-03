package com.example.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.miaosha.bean.Goods;
import com.example.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *

 *
 * @author longjian
 *
 */
public interface IGoodsService extends IService<Goods> {
   List<GoodsVo> findGoodsVo();
   GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
