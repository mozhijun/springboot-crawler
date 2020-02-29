package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    /*图片id,自增*/
    private Long imageId;
    /*经过压缩后的大图(600P)url*/
    private String bigUrl;
    /*带水印大图url*/
    private String watermarkerUrl;
    /*原图高度*/
    private Integer rawHeight;
    /*原图宽度*/
    private Integer rawWidth;
    /*缩略图url*/
    private String thumbUrl;
    /*内容编号id*/
    private Long contentId;
}