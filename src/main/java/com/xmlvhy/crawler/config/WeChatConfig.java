package com.xmlvhy.crawler.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName WeChatConfig
 * @Description TODO 微信配置类
 * @Author 小莫
 * @Date 2019/04/13 19:40
 * @Version 1.0
 **/
@Component
@PropertySource("classpath:config.properties")
@Data
public class WeChatConfig {
    /*微信开放平台生成的appId*/
    @Value("${wxopen.appid}")
    private String appId;
    /*微信开放平台生成的app secret*/
    @Value("${wxopen.appsecret}")
    private String appSecret;
    /*微信扫码登录回调地址*/
    @Value("${wxopen.redirect_url}")
    private String redirectUrl;
    /*微信开放平台二维码连接*/
    @Value("${wxopen.qrcode_url}")
    public String openQRCodeUrl;
    /*微信开放平台获取access_token地址*/
    @Value("${wxopen.access_token_url}")
    public String accessTokenUrl;
    /*获取用户信息*/
    @Value("${wxopen.user_info_url}")
    public String userInfoUrl;
}
