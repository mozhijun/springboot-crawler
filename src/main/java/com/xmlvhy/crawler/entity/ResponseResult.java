package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ResponseResult
 * @Description TODO 响应数据封装类
 * @Author 小莫
 * @Date 2019/04/09 11:25
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {

    /*响应码 0表示成功，1表示失败*/
    private Integer code;
    /*响应消息*/
    private String msg;
    /*响应数据*/
    private Object data;

    /**
     *功能描述: 成功，未带返回数据
     * @Author 小莫
     * @Date 11:38 2019/04/09
     * @Param []
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    public static ResponseResult success(){
        return new ResponseResult(0,"success",null);
    }

    /**
     *功能描述: 成功带消息
     * @Author 小莫
     * @Date 11:40 2019/04/09
     * @Param [msg]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    public static ResponseResult success(String msg){
        return new ResponseResult(0,msg,null);
    }

    /**
     *功能描述: 成功带消息和数据
     * @Author 小莫
     * @Date 11:40 2019/04/09
     * @Param [msg, data]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    public static ResponseResult success(String msg,Object data){
        return new ResponseResult(0,msg,data);
    }

    /**
     *功能描述: 成功只带数据
     * @Author 小莫
     * @Date 11:41 2019/04/09
     * @Param [data]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    public static ResponseResult success(Object data){
        return new ResponseResult(0,"success",data);
    }

    /**
     *功能描述: 失败不带消息
     * @Author 小莫
     * @Date 11:43 2019/04/09
     * @Param []
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    public static ResponseResult fail(){
        return new ResponseResult(1,"fail",null);
    }

    /**
     *功能描述: 失败带消息
     * @Author 小莫
     * @Date 11:42 2019/04/09
     * @Param [msg]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    public static ResponseResult fail(String msg){
        return new ResponseResult(1,msg,null);
    }
}
