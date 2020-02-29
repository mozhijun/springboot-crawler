package com.xmlvhy.crawler;

import com.xmlvhy.crawler.service.DataProcessedService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerBsbdjApplicationTests {

    @Autowired
    private DataProcessedService dataProcessedService;

    @Test
    public void contextLoads() {
        //解析
        dataProcessedService.sourceDataEtl();
    }

}
