package com.example.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.miaosha.bean.User;
import com.example.miaosha.exception.GlobalException;
import com.example.miaosha.mapper.UserMapper;
import com.example.miaosha.service.UserService;
import com.example.miaosha.utils.CookieUtil;
import com.example.miaosha.utils.MD5Util;
import com.example.miaosha.utils.UUIDUtil;
import com.example.miaosha.vo.LoginVo;
import com.example.miaosha.vo.ResBean;
import com.example.miaosha.vo.ResBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Service
@EnableRedisHttpSession
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public ResBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //根据电话获取用户信息
        User user = userMapper.selectById(mobile);
        //不存在用户信息
        if(null == user){
            throw new GlobalException(ResBeanEnum.LOGIN_ERROR);
        }
        //密码错误，由于从前端传过来的数据以及通过js进行过一次md5加密，盐就是1a2b3c4d，所以这里调用的加密方法就是从前台到数据库中的加密，
        // 后面只需要加密一次
        if(!MD5Util.formPassToDBPass(password, user.getSalt()).equals(user.getPassword())){
            //如果不存在用户，直接抛出异常，然后被GlobalExceptionHandler获取，获取之后判断异常类型，处理异常，返回相应的ResBean，
            // 前端ajax获取其中的code来选择输出相应错误信息。
            throw new GlobalException(ResBeanEnum.LOGIN_ERROR);
        }
        //获取一个随机32位数列，用于判定之后判定用户是否登录
        String ticket= UUIDUtil.uuid();
        //用户信息保存在redis中，30分钟过期
        redisTemplate.opsForValue().set("user:"+ticket, user);
        //在response中添加一个cookie值，用于判断获取session中user对象
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return ResBean.success(ticket);
    }
/**
* @Description: 在redis中根据cookie获取用户
        * @Param: [userTicket]，用户登录之后存储在redis中的值名称，就是登录凭证
        * @return: com.example.miaosha.bean.User
        * @Author: longjian
        * @Date: 13:24 2022/6/14
        */

    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(!StringUtils.hasLength(userTicket)){
            return null;
        }
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        User user = (User) opsForValue.get("user:"+userTicket);
        if(user!=null){
            //刷新cookie失效时长
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}
