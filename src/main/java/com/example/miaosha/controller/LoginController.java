package com.example.miaosha.controller;

import com.example.miaosha.service.UserService;
import com.example.miaosha.vo.LoginVo;
import com.example.miaosha.vo.ResBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;
    /**
     * @Description: 跳转登录页面
     * @Param:
     * @return:
     * @Author: longjian
     * @Date:15:32 2022/5/29
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }
    /** 
    * @Description:完成登录认证
            * @Param: [loginVo]，接受密码的JavaBean
            * @return: com.example.miaosha.vo.ResBean
            * @Author: longjian
            * @Date:16:33 2022/5/29
            */
    
    @RequestMapping("doLogin")
    @ResponseBody
    /**
    * @Description: 实现登录功能
            * @Param: [loginVo]，@Valid注解的作用就是确保LoginVo类中使用的validator注解生效，这个组件是我们自己导入的
            * @return: com.example.miaosha.vo.ResBean
            * @Author: longjian
            * @Date:17:46 2022/5/30
            */

    public ResBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        log.info("{}", loginVo);
        return userService.doLogin(loginVo,request,response);
    }

}
