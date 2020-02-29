package com.xmlvhy.crawler.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @ClassName MybatisConfig
 * @Description TODO mybatis 的配置类
 * @Author 小莫
 * @Date 2019/04/09 15:56
 * @Version 1.0
 **/
@Component
//扫描dao包，自动生成mapper对应的实现类
@MapperScan("com.xmlvhy.crawler.dao")
public class MybatisConfig {

    /**
     *功能描述: 配置阿里的druid连接池
     * @Author 小莫
     * @Date 16:10 2019/04/09
     * @Param []
     * @return javax.sql.DataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

}
