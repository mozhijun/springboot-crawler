package com.xmlvhy.crawler.service;

import java.util.Map;

/**
 * @Author: 小莫
 * @Date: 2019-04-09 9:59
 * @Description TODO: 爬虫业务接口
 */
public interface CrawlerService {
    void crawl (String url, Map<String,Object> paramsMap);
    void crawlRunner();
}
