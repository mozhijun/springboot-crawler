package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    /*评论ID*/
    private Long commentId;
    /*评论内容*/
    private String commentText;
    /*评论用户的id*/
    private Long uid;
    /*过审时间*/
    private String passtime;
    /*内容Id*/
    private Long contentId;
}