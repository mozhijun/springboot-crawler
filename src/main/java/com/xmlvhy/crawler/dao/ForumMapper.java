package com.xmlvhy.crawler.dao;

import com.xmlvhy.crawler.entity.Forum;

public interface ForumMapper {
    int deleteByPrimaryKey(Long forumId);

    int insert(Forum record);

    int insertSelective(Forum record);

    Forum selectByPrimaryKey(Long forumId);

    int updateByPrimaryKeySelective(Forum record);

    int updateByPrimaryKey(Forum record);
}