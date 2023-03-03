package com.example.miaosha.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    //但是此时md5加密会出现一个问题，就是盐值salt=“1a2b3c4d"，char a = salt.charAt(0)，char b = salt.charAt(2)，
    // 此时后端a+b会转为ascII码对应值49+50=99，而前端为字符串拼接=12。所以我们不能直接使用自己测试的出来的数据写入到数据库里面，出现的数据会不一致
    public static String md5(String str){
        return DigestUtils.md2Hex(str);
    }
    //md5加密盐
    private static final String SALT="1a2b3c4d";
    //明文第一次加密
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+SALT.charAt(0)+SALT.charAt(2) + inputPass
                +SALT.charAt(5) + SALT.charAt(4);
        return md5(str);
    }
    //第一次加密之后的数据在进行一次加密
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5)
                + salt.charAt(4);
        return md5(str);
    }
    //直接明文两次加密
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        return formPassToDBPass(formPass, saltDB);
    }
}
