package com.xmlvhy.crawler.dao;

import com.xmlvhy.crawler.entity.Video;

import java.util.List;
import java.util.Map;

public interface VideoMapper {
    int deleteByPrimaryKey(Long videoId);

    int insert(Video record);

    int insertSelective(Video record);

    Video selectByPrimaryKey(Long videoId);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKey(Video record);

    List<Map<String,Object>> selectByContentId(Long contentId);
}