package com.example.miaosha.controller;

import com.example.miaosha.bean.User;
import com.example.miaosha.service.IGoodsService;
import com.example.miaosha.service.UserService;
import com.example.miaosha.utils.UserUtils;
import com.example.miaosha.vo.DetailVo;
import com.example.miaosha.vo.GoodsVo;
import com.example.miaosha.vo.ResBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author longjian
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    /**
    * @Description: 实现查询所有的可秒杀的商品
     *      RequestMapping中produces代表方法生产出这种类型的东西
            * @Param: [session, model, request]
            * @return: java.lang.String
            * @Author: longjian
            * @Date: 10:09 2022/6/5
            */

    @RequestMapping(value = "toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList( Model model, HttpServletRequest request, HttpServletResponse response
            ,@CookieValue("userTicket") String ticket){
        //没有用户消息就返回登录页
        User user = userService.getUserByCookie(ticket, request, response);
        if(null==user){
            return "login";
        }
        //这里是把页面缓存到redis里面，加快用户访问速度提高qps
        //从redis里面获取页面，不为空就直接返回页面，缓存的是解析后的页面，直接返回前端就能正常显示的那种
        ValueOperations<String,String> opsForValue = redisTemplate.opsForValue();
        String html = opsForValue.get("goodsList");
        if(StringUtils.hasLength(html)){
            return html;
        }
        //添加用户信息和商品信息到页面里面
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        //如果页面为空，就手动渲染页面且存入redis
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if(StringUtils.hasLength(html)){
            //页面缓存存入redis，有效时长60秒，防止用户看到的都是过期很久的数据
            opsForValue.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "toDetail/{goodsId}")
    @ResponseBody
    public ResBean toDetail(@PathVariable("goodsId")Long goodsId, HttpServletRequest request,
                            HttpServletResponse response, @CookieValue("userTicket") String ticket) {
        User user = userService.getUserByCookie(ticket, request, response);
        //添加商品详情
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date now = new Date();
        //秒杀状态，0未开始，1进行中，2已结束
        int secKillStatus = 0;
        //剩余开始时间，-1表示已结束
        int remainSeconds = 0;
        if (now.before(startDate)) {
            //未开始
            //转化为时间戳进行差值运算
            remainSeconds = (int) (startDate.getTime() - now.getTime()) / 1000;
        } else if (now.after(endDate)) {
            //已结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            //进行中
            secKillStatus = 1;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(secKillStatus);
        return ResBean.success(detailVo);
    }
}
