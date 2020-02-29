package com.xmlvhy.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //开启任务调度，使用定时任务
public class CrawlerBsbdjApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerBsbdjApplication.class, args);
    }

}
