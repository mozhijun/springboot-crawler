package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /*用户id*/
    private Long uid;
    /*用户头像url*/
    private String header;
    /*是够是vip 0否 1是*/
    private Integer isVip;
    private Integer isV;
    /*房间url*/
    private String roomUrl;
    /*房间名称*/
    private String roomName;
    /*房间角色*/
    private String roomRole;
    /*房间logo*/
    private String roomIcon;
    /*用户昵称*/
    private String nickname;
}