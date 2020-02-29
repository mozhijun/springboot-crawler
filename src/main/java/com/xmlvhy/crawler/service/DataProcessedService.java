package com.xmlvhy.crawler.service;

import com.xmlvhy.crawler.entity.Source;

import java.util.Map;

/**
 * @Author: 小莫
 * @Date: 2019-04-09 18:34
 * @Description TODO: 源数据处理业务层
 */
public interface DataProcessedService {

    void sourceDataEtl();

    void createContent(Source source, Map<String,Object> contentMap);

}
