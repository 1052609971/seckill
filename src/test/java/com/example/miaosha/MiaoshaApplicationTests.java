package com.example.miaosha;

import com.example.miaosha.utils.MD5Util;
import com.example.miaosha.vo.GoodsVo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.DriverManager;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
//先将测试类不执行
@Disabled
class MiaoshaApplicationTests {
    @Test
    public void testMD5(){
        System.out.println(MD5Util.inputPassToFormPass("200105"));
        System.out.println(MD5Util.formPassToDBPass("1f0f4029744aba1310969dd4bd0937f5", "1a2b3c4d"));
        System.out.println(MD5Util.inputPassToDbPass("200105", "1a2b3c4d"));
        String s="200105";
        String b=""+s.charAt(1)+s.charAt(5);
        System.out.println(b);
    }
}
