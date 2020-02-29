package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forum {
    /*圈子id*/
    private Long forumId;
    /*发帖数量*/
    private Integer postCount;
    /*Logo Url*/
    private String logo;
    /*频道顺序*/
    private Integer forumSort;
    /*圈子状态*/
    private Integer forumStatus;
    /*圈子简介*/
    private String info;
    /*圈子名称*/
    private String name;
    /*圈子参与的用户数量*/
    private Integer userCount;
}