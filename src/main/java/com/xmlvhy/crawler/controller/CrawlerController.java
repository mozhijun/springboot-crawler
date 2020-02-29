package com.xmlvhy.crawler.controller;

import com.xmlvhy.crawler.entity.ResponseResult;
import com.xmlvhy.crawler.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName CrawlerController
 * @Description TODO： 爬虫接口
 * @Author 小莫
 * @Date 2019/04/09 11:22
 * @Version 1.0
 **/
@Controller
@RequestMapping("/api/v1")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @RequestMapping("crawlerSource")
    @ResponseBody
    public ResponseResult crawlerSource() {
        crawlerService.crawlRunner();
        return ResponseResult.success();
    }
}
