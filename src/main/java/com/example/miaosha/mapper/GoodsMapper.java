package com.example.miaosha.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.miaosha.bean.Goods;
import com.example.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *

 *
 * @author longjian
 *
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
