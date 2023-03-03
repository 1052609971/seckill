package com.example.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.miaosha.bean.User;
import com.example.miaosha.vo.LoginVo;
import com.example.miaosha.vo.ResBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService  extends IService<User> {
    ResBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);
}
