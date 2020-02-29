package com.xmlvhy.crawler.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xmlvhy.crawler.dao.SourceMapper;
import com.xmlvhy.crawler.entity.Source;
import com.xmlvhy.crawler.service.CrawlerService;
import com.xmlvhy.crawler.service.DataProcessedService;
import com.xmlvhy.crawler.utils.OgnlUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CrawlerServiceImpl
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/09 10:01
 * @Version 1.0
 **/
@Service
@Slf4j
public class CrawlerServiceImpl implements CrawlerService {

    @Value("${crawler.urls}")
    private String urls;

    @Value("${crawler.num}")
    private Integer num;

    @Value("${crawler.crawlerSum}")
    private Integer crawlerSum;

    @Autowired
    private SourceMapper sourceDao;

    @Autowired
    private DataProcessedService dataProcessedService;

    /**
     * 功能描述: 执行爬虫任务
     *
     * @return void
     * @Author 小莫
     * @Date 16:33 2019/04/09
     * @Param [url, paramsMap]
     * url:表示爬取的url
     * paramsMap表示参数集
     */
    @Override
    public void crawl(String url, Map<String, Object> paramsMap) {
        //拿到对应参数
        Integer channelId = (Integer) paramsMap.get("channelId");
        Integer count = (Integer) paramsMap.get("count");
        String np = (String) paramsMap.get("np");

        //每一次根据规则更新url
        url = url.replace("{np}", np);
        log.info("当前爬取的url是: {}, channelId:{}", url, channelId);

        //1.创建okHttp 对象
        OkHttpClient client = new OkHttpClient();
        //2.构建请求，设置要访问的url
        Request.Builder builder = new Request.Builder().url(url);
        //设置请求头
        builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");
        //创建设置好的请求对象
        Request request = builder.build();

        String result = null;
        //每个模块爬取的url设置循环爬取 num 次，超过num次未爬取到数据则失败
        for (int i = 0; i < num; i++) {
            log.info("正在执行第{}次爬取任务", i + 1);
            try {
                //3.发送请求,返回响应对象
                Response response = client.newCall(request).execute();
                //获取返回结果
                result = response.body().string();
                //当前爬取成功则,则跳出当前循环
                break;
            } catch (IOException e) {
                e.printStackTrace();
                log.info("爬虫链接超时，正在准备第{}次重试,当前URL: {}", (i + 1), url);
                continue;
            }
        }

        //num 次抓取都失败,程序中断
        if (result == null) {
            log.warn("爬虫抓取失败，原因：链接超时，URL: {}", url);
        }

        Gson gson = new Gson();
        Map<String, Object> resultMap = gson.fromJson(result, new TypeToken<Map>() {
        }.getType());
        List<Map<String, Object>> list = OgnlUtil.getListMap("list", resultMap);
        //获取np的内容，np是double类型，需要转化以下
        Double tempNp = (Double) OgnlUtil.getNumber("info.np", resultMap);
        //DecimalFormat代表对数字进行格式化，按要求显示 #号代表有这个数字则显示，没有则忽略 。0 ,代表没有的时候使用0占位
        //  123456.78        #########  -> 123456    #########.## ->123456.78   0000000000.000 -> 0000123456.780
        if (null != tempNp) {
            String strNp = new DecimalFormat("############").format(tempNp);
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("np", strNp);

            //保存原始数据
            Source source = new Source();
            source.setChannelId(Long.valueOf(channelId));
            source.setResponseText(result);
            source.setState("WAITTING");
            source.setUrl(url);
            source.setCreateTime(new Date());
            sourceDao.insert(source);
            log.info("数据抓取成功，NP:{},URL:{}", strNp, url);

            //设置爬取结束条件，根据爬取的条目总数，每个模块爬取100条数据,每次可以爬取25条
            count = count + list.size();
            tempMap.put("count", count);
            tempMap.put("channelId", channelId);

            //只抓取最近的100条数据，每小时抓一次。大约百思每小时更新3~5条数据，所以抓最近的100条足够了
            log.info("当前爬取了{}条数据", count);
            if (count >= crawlerSum) {
                log.info("当前np:{} , 爬虫任务完成", strNp);
                return;
            }
            //利用递归进行数据抓取
            crawl(url, tempMap);
        }
    }

    /**
     * 功能描述: 爬虫任务执行
     *
     * @return void
     * @Author 小莫
     * @Date 16:32 2019/04/09
     * @Param []
     */
    @Override
    public void crawlRunner() {
        String[] crawlerUrls = urls.split(",");
        Map<String, Object> parmsMap = new HashMap<>();
        //依次爬取推荐、视频、图片、笑话以及排行的数据
        for (int i = 0; i < crawlerUrls.length; i++) {
            log.info("开始爬取第 {}模块的数据", i + 1);
            String url = crawlerUrls[i];
            parmsMap.put("channelId", i + 1);
            parmsMap.put("count", 0);
            parmsMap.put("np", "0");
            this.crawl(url, parmsMap);
        }
        //数据爬取完成后，开始处理数据
        log.info("============开始处理源数据============");
        Long startTime = System.currentTimeMillis();
        dataProcessedService.sourceDataEtl();
        Long endTime = System.currentTimeMillis();
        log.info("============源数据处理完成============");
        log.info("============当前处理耗时：{} 秒============", (endTime - startTime) / 1000);
    }
}
