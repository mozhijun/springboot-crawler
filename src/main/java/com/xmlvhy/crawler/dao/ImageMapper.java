package com.xmlvhy.crawler.dao;

import com.xmlvhy.crawler.entity.Image;

import java.util.List;
import java.util.Map;

public interface ImageMapper {
    int deleteByPrimaryKey(Long imageId);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(Long imageId);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);

    List<Map<String,Object>> selectByContentId(Long contentId);
}