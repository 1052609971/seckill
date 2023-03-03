package com.example.miaosha.config;

import com.example.miaosha.interceptors.CheckLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

/**
* @Description:
        mvc配置类
        * @Author: longjian
        * @Date: 21:24 2022/5/31
        */
@Configuration
public class WebConf implements WebMvcConfigurer {
        /**
        * @Description: 设置自定义拦截器，用于拦截未登录用户请求相关页面
                * @Param: [registry]
                * @return: void
                * @Author: longjian
                * @Date: 22:05 2022/5/31
                */
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                CheckLoginInterceptor checkLoginInterceptor = new CheckLoginInterceptor();
                //在配置拦截器的时候，/*表示所有的/hello之类的单层页面，/**是所有的页面例如/a/vc/c/x/s，也就是多层页面
                ArrayList<String> include = new ArrayList<>();
                //拦截商品详情页面页面
                include.add("/goods/*");
                include.add("/seckill/*");
                registry.addInterceptor(checkLoginInterceptor).addPathPatterns(include);
        }
}
