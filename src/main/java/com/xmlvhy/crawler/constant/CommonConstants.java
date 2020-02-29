package com.xmlvhy.crawler.constant;

/**
 * @Author: 小莫
 * @Date: 2019-04-09 11:34
 * @Description TODO 通用常量
 */
public interface CommonConstants {
        /*表示等待处理*/
       String SOURCE_STATE_WAIT = "WAITTING";
       /*表示已经处理完成*/
       String SOURCE_STATE_PROCESS = "PROCESSED";
       /*JWT 主题*/
       String JWT_SUBJECT = "crawler";
       /*过期时间，设置为一周*/
       long JWT_TOKEN_EXPIRE_TIME = 1000*60*60*24*7;
       /*秘钥*/
       String JWT_APPSECRET = "mzj584wanlhy";
}
