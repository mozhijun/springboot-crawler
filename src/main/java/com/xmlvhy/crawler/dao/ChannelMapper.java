package com.xmlvhy.crawler.dao;

import com.xmlvhy.crawler.entity.Channel;

public interface ChannelMapper {
    int deleteByPrimaryKey(Long channelId);

    int insert(Channel record);

    int insertSelective(Channel record);

    Channel selectByPrimaryKey(Long channelId);

    int updateByPrimaryKeySelective(Channel record);

    int updateByPrimaryKey(Channel record);
}