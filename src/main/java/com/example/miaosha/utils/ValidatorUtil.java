package com.example.miaosha.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @Description:
        * 校验用户传过来的是否是手机号
        * @Author: longjian
        * @Date:16:56 2022/5/29
        */

public class ValidatorUtil {
    //正则表达式，1开头，第二位规定3-9，之后九位0-9
    private static final Pattern mobile_pattern=Pattern.compile("[1]([3-9])[0-9]{9}$");
    public static boolean isMobile(String mobile){
        if(!StringUtils.hasLength(mobile)){
            //字符串为空
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(mobile);
        //符合正则表达式匹配就为true
        return matcher.matches();
    }
}
