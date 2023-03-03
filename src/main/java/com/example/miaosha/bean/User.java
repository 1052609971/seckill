package com.example.miaosha.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("t_user")
public class User implements Serializable {
    private static final long serialVersionUID = -1;
    private Long id;
    private String nickname;
    private String password;
    private String head;
    private String salt;
    private Timestamp registerDate;
    private Timestamp lastLoginDate;
    private int loginCount;
}
