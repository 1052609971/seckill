package com.example.miaosha.controller;

import com.example.miaosha.bean.User;
import com.example.miaosha.vo.ResBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
* @Description: 测试用的类
        * @Param:
        * @return:
        * @Author: longjian
        * @Date:8:35 2022/6/16
        */

@Controller
@RequestMapping("user")
public class UserController {
    @ResponseBody
    @RequestMapping("info")
    public ResBean userInfo(User user){
        return ResBean.success(user);
    }
}
