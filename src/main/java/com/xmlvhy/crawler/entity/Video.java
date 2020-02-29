package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    /*视频id 自增*/
    private Long videoId;
    /*视频播放url*/
    private String videoUrl;
    /*视频下载url*/
    private String downloadUrl;
    /*视频播放宽度*/
    private Integer width;
    /*视频播放高度*/
    private Integer height;
    /*视频播放量*/
    private Integer playfcount;
    /*播放时长，单位秒*/
    private Integer duration;
    private Integer playcount;
    /*视频缩略图，等比例*/
    private String thumb;
    /*视频缩略图 正方*/
    private String thumbSmall;
    /*内容id*/
    private Long contentId;
}