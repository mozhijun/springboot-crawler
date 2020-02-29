package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    /*内容id*/
    private Long contentId;
    /*频道id*/
    private Long channelId;
    /*圈子id*/
    private Long forumId;
    /*用户id*/
    private Long uid;
    /*状态 4 过审*/
    private Integer status;
    /*评论数量*/
    private Integer commentCount;
    /*收藏数量*/
    private Integer bookmarkCount;
    /*标题或者正文*/
    private String contentText;
    /*点赞数量*/
    private Integer likeCount;
    /*踩的数量*/
    private Integer hateCount;
    /*分享url*/
    private String shareUrl;
    /*分享数量*/
    private Integer shareCount;
    /*发布时间*/
    private String passtime;
    /*内容类型 text/image/video/gif*/
    private String contentType;
    /*数据源id*/
    private Long sourceId;
    /*创建时间*/
    private Date createTime;
}