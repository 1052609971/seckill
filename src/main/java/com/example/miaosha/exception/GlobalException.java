package com.example.miaosha.exception;

import com.example.miaosha.vo.ResBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Description: 全局异常类
        * @Param:
        * @return:
        * @Author: longjian
        * @Date:13:31 2022/5/31
        */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private ResBeanEnum resBeanEnum;
}
