package com.xmlvhy.crawler.service;

import com.github.pagehelper.Page;
import com.xmlvhy.crawler.entity.Content;

import java.util.List;
import java.util.Map;

/**
 * @Author: 小莫
 * @Date: 2019-04-10 10:13
 * @Description TODO
 */
public interface ContentService {

    List<Map<String,Object>> getAllContents();

    Page<Map<String,Object>> getAllContentsByPage(Integer page,Integer pageSize);

    Page<Map<String,Object>> getAllContentsByPageAndParams(Integer page, Integer limit,Integer channelId,String contentType,String keyword);

    Boolean removeContent(Long contentId);

    Map<String,Object> getPreviewData(Long contentId);

    Content findContentByContentId(Long contentId);
}
