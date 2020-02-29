package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Source {
    /*数据源id*/
    private Long sourceId;
    /*频道id*/
    private Long channelId;
    /*爬取的源数据*/
    private String responseText;
    /*创建时间*/
    private Date createTime;
    /*url*/
    private String url;
    /*状态 WAITTING 表示待处理状态，PROCESSED 表示已处理*/
    private String state;
}