package com.xmlvhy.crawler.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/08 22:33
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TestController {

    @RequestMapping("test")
    public void crawlerTest(){
    /*以下四个连接爬取的数据已经被加密，这里使用之前的api接口*/
    //推荐
    //String url = "http://s.budejie.com/v2/topic/list/1/0-0/budejie-android-8.0.5/0-25.json";
    ////视频
    //String video_url = "http://s.budejie.com/v2/topic/list/41/0-0/budejie-android-8.0.5/0-25.json";
    ////图片
    //String image_url = "http://s.budejie.com/v2/topic/list/10/0-0/budejie-android-8.0.5/0-25.json";
    ////笑话
    //String joke_url = "http://s.budejie.com/v2/topic/list/29/0-0/budejie-android-8.0.5/0-25.json";

    // URL模板
    String[] templates = new String[]{
            "http://c.api.budejie.com/topic/list/jingxuan/1/budejie-android-6.9.4/0-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
            , "http://c.api.budejie.com/topic/list/jingxuan/41/budejie-android-6.9.4/0-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
            , "http://c.api.budejie.com/topic/list/jingxuan/10/budejie-android-6.9.4/0-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
            , "http://c.api.budejie.com/topic/list/jingxuan/29/budejie-android-6.9.4/0-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
            , "http://s.budejie.com/topic/list/remen/1/budejie-android-6.9.4/0-20.json?market=xiaomi&ver=6.9.4&visiting=&os=6.0&appname=baisibudejie&client=android&udid=864141036474044&mac=02%3A00%3A00%3A00%3A00%3A00"
    };
    //    for(int i = 0; i< templates.length;i++){
    //        String url = templates[i];
    //        //1.创建okHttp 对象
    //        OkHttpClient client = new OkHttpClient();
    //        //2.构建请求，设置要访问的url
    //        Request.Builder builder = new Request.Builder().url(url);
    //        //设置请求头
    //        builder.addHeader("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");
    //        //创建设置好的请求对象
    //        Request request = builder.build();
    //        String result = null;
    //
    //        try {
    //            log.info("正在爬取第 {} 个模块的数据",i+1);
    //            //3.发送请求
    //            Response response = client.newCall(request).execute();
    //            result = response.body().string();
    //            log.info("第{}个模块，URL:{} ,爬取的数据：{}",i+1,url,result);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //            log.info("第 {} 个模块数据爬取失败，URL: {} ,原因：链接超时",i+1,url);
    //            continue;
    //        }
    //    }
    }
}
