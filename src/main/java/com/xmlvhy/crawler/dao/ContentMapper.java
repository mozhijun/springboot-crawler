package com.xmlvhy.crawler.dao;

import com.xmlvhy.crawler.entity.Content;

import java.util.List;
import java.util.Map;

public interface ContentMapper {
    int deleteByPrimaryKey(Long contentId);

    int insert(Content record);

    int insertSelective(Content record);

    Content selectByPrimaryKey(Long contentId);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKey(Content record);

    List<Map<String,Object>> selectAll();

    List<Map<String,Object>> selectByParams(Map<String, Object> params);
}