package com.example.miaosha.controller;

import com.example.miaosha.bean.Order;
import com.example.miaosha.bean.SeckillMessage;
import com.example.miaosha.bean.User;
import com.example.miaosha.mq.SecKillConsumer;
import com.example.miaosha.mq.SecKillSyncProducer;
import com.example.miaosha.service.IGoodsService;
import com.example.miaosha.service.IOrderService;
import com.example.miaosha.service.ISeckillOrderService;
import com.example.miaosha.service.UserService;
import com.example.miaosha.service.impl.GoodsServiceImpl;
import com.example.miaosha.service.impl.OrderServiceImpl;
import com.example.miaosha.vo.GoodsVo;
import com.example.miaosha.vo.ResBean;
import com.example.miaosha.vo.ResBeanEnum;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {
    @Autowired
    private GoodsServiceImpl goodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQProducer producer;

    /** 
    * @Description: 秒杀订单排队
            * @Param: [request, model, goodsId]
            * @return: java.lang.String
            * @Author: longjian
            * @Date: 16:29 2022/6/5
            */
    @RequestMapping("/doSeckill")
    @ResponseBody
    public ResBean doSeckill(HttpServletRequest request, @RequestParam("goodsId") Long goodsId,
                             @CookieValue("userTicket") String ticket, HttpServletResponse response) throws Exception {
        User user = userService.getUserByCookie(ticket,request,response);
        if(null==user){
            //未登录，跳转登录页面
            return ResBean.error(ResBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购
        Order secKillOrder = (Order) valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if(secKillOrder!=null){
            return ResBean.error(ResBeanEnum.REPEATE_ERROR);
        }
        //预减库存
        Long stock = valueOperations.decrement("secKillGoods:" + goodsId);
        if(stock<0){
            //这里加一是为了防止在redis中出现负数，好看一点
            valueOperations.increment("secKillGoods:" + goodsId);
            //写进去库存空余了
            valueOperations.setIfAbsent("isStockEmpty:" + goodsId, "this kind of goods sold out");
            return ResBean.error(ResBeanEnum.EMPTY_STOCK);
        }
        //订单到mq排队
        SecKillSyncProducer.rankSecOrder(new SeckillMessage(user, goodsId),producer);
        //立即返回0，代表订单成功进入排队
        return ResBean.success(0);
    }
    @RequestMapping("result")
    @ResponseBody
    public ResBean getDetail(Long goodsId,@CookieValue("userTicket") String ticket,
                             HttpServletRequest request,HttpServletResponse response){
        User user = userService.getUserByCookie(ticket,request,response);
        if(null==user){
            //未登录，跳转登录页面
            return ResBean.error(ResBeanEnum.SESSION_ERROR);
        }
        Long detail = seckillOrderService.getDetail(user, goodsId);
        return ResBean.success(detail);
    }
    /** 
    * @Description: 系统初始化之后把商品库存加载到redis里面去
            * @Param: []
            * @return: void
            * @Author: longjian
            * @Date: 17:06 2022/6/15
            */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVo = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(goodsVo)){
            //没有库存直接返回
            return;
        }else{
            for (GoodsVo g:goodsVo) {
                //把所有的库存读取到redis里面
                redisTemplate.opsForValue().set("secKillGoods:"+g.getId(), g.getStockCount());
            }
        }
    }
}
