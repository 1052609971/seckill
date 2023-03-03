package com.example.miaosha.utils;

import com.example.miaosha.bean.User;
import com.example.miaosha.vo.ResBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserUtils {
    public static User getUserByRequest(HttpServletRequest request) {
        String userTicket = CookieUtil.getCookieValue(request, "userTicket");
        return (User) request.getSession().getAttribute(userTicket);
    }

    private static void createUser(int count,int begin,int end) throws Exception {
        List<User> users = new ArrayList<>(count);
        //生成用户
        for (int i = begin; i < end; i++) {
            User user = new User();
            user.setId(13000000000L + i);
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterDate(new Timestamp(System.currentTimeMillis()));
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDbPass("200105", user.getSalt()));
            users.add(user);
        }
            System.out.println("create user");
//            //插入数据库
//            Connection conn = getConn();
//            String sql = "insert into t_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//        for (User user : users) {
//            pstmt.setInt(1, user.getLoginCount());
//            pstmt.setString(2, user.getNickname());
//            pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
//            pstmt.setString(4, user.getSalt());
//            pstmt.setString(5, user.getPassword());
//            pstmt.setLong(6, user.getId());
//            pstmt.addBatch();
//        }
//            pstmt.executeBatch();
//            pstmt.close();
//            conn.close();
//            System.out.println("insert to db");
//            登录，生成token
            String urlString = "http://localhost:8080/login/doLogin";
            File file = new File("C:\\Users\\adimn\\Desktop\\config.txt");
            if (file.exists()) {
                file.delete();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            file.createNewFile();
            raf.seek(0);
        for (User user : users) {
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("200105");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = bout.toString();
            ObjectMapper mapper = new ObjectMapper();
            ResBean respBean = mapper.readValue(response, ResBean.class);
            String userTicket = ((String) respBean.getObj());
            System.out.println("create userTicket : " + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();
        System.out.println("over");
     }

    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/miaosha?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "200105";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {

        createUser(1000, 0, 999);
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS,new ArrayBlockingQueue<>(20),Executors.defaultThreadFactory());
//        threadPoolExecutor.execute(new Runnable() {
//            @SneakyThrows
//            @Override
//            public void run() {
//                createUser(500, 0, 499);
//            }
//        });
//        threadPoolExecutor.execute(new Runnable() {
//            @SneakyThrows
//            @Override
//            public void run() {
//                createUser(500, 500, 999);
//            }
//        });
//        threadPoolExecutor.execute(new Runnable() {
//            @SneakyThrows
//            @Override
//            public void run() {
//                createUser(500, 1000, 1499);
//            }
//        });
//        threadPoolExecutor.execute(new Runnable() {
//            @SneakyThrows
//            @Override
//            public void run() {
//                createUser(500, 1500, 1999);
//            }
//        });
//        threadPoolExecutor.execute(new Runnable() {
//            @SneakyThrows
//            @Override
//            public void run() {
//                createUser(500, 2000, 2499);
//            }
//        });
    }
}