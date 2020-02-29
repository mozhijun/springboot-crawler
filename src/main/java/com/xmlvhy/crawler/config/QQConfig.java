package com.xmlvhy.crawler.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName QQConfig
 * @Description TODO qq授权相关配置类
 * @Author 小莫
 * @Date 2019/04/19 13:11
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class QQConfig {

    @Value("${qq.app_id}")
    private String appId;
    @Value("${qq.app_key}")
    private String appKey;
    @Value("${qq.redirect_url}")
    private String redirectUrl;
    @Value("${qq.oauth_url}")
    private String oauthUrl;
    @Value("${qq.access_token_url}")
    private String accessTokenUrl;
    @Value("${qq.open_id_url}")
    private String openIdUrl;
    @Value("${qq.user_info_url}")
    private String userInfoUrl;

}
