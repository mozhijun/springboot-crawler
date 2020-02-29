package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    /*主键*/
    private Integer id;
    /*用户姓名*/
    private String name;
    /*用户密码*/
    private String password;
    /*用户微信uid*/
    private String uid;
    /*用户昵称*/
    private String nickname;
    /*用户头像*/
    private String header = "https://www.xmlvhy.com/images/user.png";
    /*用户登账户名*/
    private String loginName;
    /*用户签名*/
    private String sign;
    /*用户性别*/
    private String sex;
    /*用户创建时间*/
    private Date createTime;

}