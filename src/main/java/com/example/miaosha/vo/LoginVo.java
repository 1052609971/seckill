package com.example.miaosha.vo;

import com.example.miaosha.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
* @Description: 这是用户登录参数接受类
        * @Author: longjian
        * @Date: 17:13 2022/5/30
        */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    @NotNull
    /*
    * @Description: 自定义注解，判断手机号是否符合规则
            * @Author: longjian
            * @Date:19:09 2022/5/30
            */

    @IsMobile
    private String mobile;

    @NotNull
    //长度最小32位，因为经过md5加密
    @Length(min = 32)
    private String password;
}
