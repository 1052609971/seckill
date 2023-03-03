package com.example.miaosha.vo;

import com.example.miaosha.utils.ValidatorUtil;
import com.example.miaosha.validator.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
* @Description: 判断手机号码判断规则
 *       @IsMobile 注解约束规则，用于处理判断是否符合手机号规则
        * @Author: longjian
        * @Date: 18:252022/5/30
        */

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
    private boolean required=false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
/**
* @Description: 判断手机号码是否正确
        * @Param: [value, context]
        * @return: boolean
        * @Author: longjian
        * @Date: 18:28 2022/5/30
        */

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //要求必填的情况，不用判断字符串空了
        if(required){
            //调用ValidatorUtil中定义的判定方法
            return ValidatorUtil.isMobile(value);
        }else {
            //没要求必填的话
            if(!StringUtils.hasLength(value)){
                //字符串位空，直接返回true，代表required位false时字符串可以为空
                return true;
            }else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
