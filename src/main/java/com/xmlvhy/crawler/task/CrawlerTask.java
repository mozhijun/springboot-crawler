package com.xmlvhy.crawler.task;

import com.xmlvhy.crawler.service.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName CrawlerTask
 * @Description TODO 爬虫定时任务，定时爬取数据
 * @Author 小莫
 * @Date 2019/04/10 17:17
 * @Version 1.0
 **/
@Component
@Slf4j
public class CrawlerTask {

    @Autowired
    private CrawlerService crawlerService;
    @Value("${crawler.enable}")
    private Integer enable;
    //任务执行的方法
    //@Scheduled 注解用于指定当前方法是一个任务调度 cron表达式指定执行的间隔
    //* * * * * ?  每秒执行一次
    //秒     分      时      日     月     星期
    //*代表任意时间    具体的数字代表精确值      ,用于多个值     /用于固定间隔    - 范围  ?忽略
    //0  * 23 * * ?     每天23点~24点之间每分钟执行一次
    //0  0 8-18  * ?    每天早上8点到下午六点，0份0秒准时执行一次
    //0  0  0  */2 * ?  0  2  4  6 ... 22  0点0分执行
    @Scheduled(cron = "${crawler.cron}")
    //@Scheduled(cron = "0 /30 * * * ?")
    public void crawlerTask(){
        //判断是否开启爬虫任务
        if (enable == 0) {
            log.warn("爬虫任务已被禁止，如需开启请将 crawler.enable 设置为 1");
            return;
        }
        //开启爬虫任务以及数据源处理
        crawlerService.crawlRunner();
    }
}
