package com.xmlvhy.crawler.dao;

import com.xmlvhy.crawler.entity.Source;

import java.util.List;

public interface SourceMapper {
    int deleteByPrimaryKey(Long sourceId);

    int insert(Source record);

    int insertSelective(Source record);

    Source selectByPrimaryKey(Long sourceId);

    int updateByPrimaryKeySelective(Source record);

    int updateByPrimaryKey(Source record);

    List<Source> findAllByState(String state);
}