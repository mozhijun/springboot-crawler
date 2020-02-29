package com.xmlvhy.crawler.interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebConfig
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/12 18:47
 * @Version 1.0
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/api/v1/**")
                .addPathPatterns("/manage/center")
                .excludePathPatterns("/crawler")
                .excludePathPatterns("/login")
                .excludePathPatterns("/static/**");
    }
}
