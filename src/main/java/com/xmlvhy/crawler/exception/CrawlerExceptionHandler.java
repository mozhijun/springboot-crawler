package com.xmlvhy.crawler.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ClassName CrawlerExceptionHandler
 * @Description TODO 全局异常捕获类
 * @Author 小莫
 * @Date 2019/04/14 11:32
 * @Version 1.0
 **/
@ControllerAdvice
public class CrawlerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(){
        return "error/5xx";
    }
}
