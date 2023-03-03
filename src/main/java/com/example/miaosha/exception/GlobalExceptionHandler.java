package com.example.miaosha.exception;

import com.example.miaosha.vo.ResBean;
import com.example.miaosha.vo.ResBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
* @Description: 处理全局异常类
        * @Author: longjian
        * @Date: 13:372022/5/31
        */

@RestControllerAdvice
/*这是处理控制器中出现异常的一种注解，是springmvc提供的
 * */
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResBean exceptionHandler(Exception e){
        if(e instanceof GlobalException){
            //如果异常是自定义的全局异常，那么就获取当前实例的ResBeanEnum，返回响应的ResBean
            GlobalException ge= (GlobalException) e;
            return ResBean.error(ge.getResBeanEnum());
        }else if(e instanceof org.springframework.validation.BindException){
            //如果是手机格式错误，返回参数校验异常类信息
            BindException be= (BindException) e;
            ResBean error = ResBean.error(ResBeanEnum.BIND_ERROR);
            //获取在自定义的注解IsMobile中定义的String，默认的就是“手机号格式错误”
            error.setMessage("参数校验异常:"+be.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return error;
        }
        //其他控制器错误就是默认的服务器异常代码500
        return ResBean.error(ResBeanEnum.ERROR);
    }
}
