package com.xmlvhy.crawler.exception;

/**
 * @ClassName CrawlerException
 * @Description TODO 自定义异常类
 * @Author 小莫
 * @Date 2019/04/12 17:53
 * @Version 1.0
 **/
public class CrawlerException extends RuntimeException{

    public CrawlerException() {
        super();
    }

    public CrawlerException(String message) {
        super(message);
    }

    public CrawlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
