package com.example.miaosha.controller;


import com.example.miaosha.bean.Goods;
import com.example.miaosha.bean.Order;
import com.example.miaosha.bean.User;
import com.example.miaosha.mapper.GoodsMapper;
import com.example.miaosha.mapper.OrderMapper;
import com.example.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 订单控制器
 * @author longjian
 */
@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private UserService userService;
	/**
	 * 功能描述: 订单详情
	 *
	 * @since:  1.0.0
	 * @Author: longjian
	 */
	@RequestMapping("/detail")
	public String detail(Long orderId, @CookieValue("userTicket") String ticket,
						 HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = userService.getUserByCookie(ticket,request,response);
		if(null==user){
			//未登录，跳转登录页面
			return "login";
		}
		Order order = orderMapper.selectById(orderId);
		Goods goods = goodsMapper.selectById(order.getGoodsId());
		model.addAttribute("goods", goods);
		model.addAttribute("order",order);
		return "orderDetail";
	}
}
