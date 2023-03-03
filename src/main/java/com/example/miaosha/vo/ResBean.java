package com.example.miaosha.vo;

import lombok.*;

/**返回前端对象
 * @author longjian
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResBean {
    private long code;
    private String message;
    private Object obj;
    /**
    * @Description: 成功返回结果
            * @Param: []
            * @return: com.example.miaosha.vo.ResBean
            * @Author: longjian
            * @Date:15:58 2022/5/29
            */
    
    public static ResBean success(){
        return new ResBean(ResBeanEnum.SUCCESS.getCode(), ResBeanEnum.SUCCESS.getMessage(), null);
    }
    /**
     * @Description: 成功返回结果
     * @Param: []
     * @return: com.example.miaosha.vo.ResBean
     * @Author: longjian
     * @Date:15:58 2022/5/29
     */

    public static ResBean success(Object obj){
        return new ResBean(ResBeanEnum.SUCCESS.getCode(), ResBeanEnum.SUCCESS.getMessage(), obj);
    }
    /** 
    * @Description: 失败返回结果
            * @Param: [resBeanEnum]
            * @return: com.example.miaosha.vo.ResBean
            * @Author: longjian
            * @Date:16:01 2022/5/29
            */
    
    public static ResBean error(ResBeanEnum resBeanEnum){
        return new ResBean(resBeanEnum.getCode(),resBeanEnum.getMessage(), null);
    }
    /** 
    * @Description: 失败返回结果
            * @Param: [resBeanEnum, obj]
            * @return: com.example.miaosha.vo.ResBean
            * @Author: longjian
            * @Date:16:01 2022/5/29
            */
    
    public static ResBean error(ResBeanEnum resBeanEnum,Object obj){
        return new ResBean(resBeanEnum.getCode(), resBeanEnum.getMessage(), obj);
    }
}

