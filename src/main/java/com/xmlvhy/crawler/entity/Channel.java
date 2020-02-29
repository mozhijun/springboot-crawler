package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    /*频道id*/
    private Long channelId;
    /*频道名称*/
    private String channelName;
    /*频道顺序*/
    private Integer sortNum;
}