package com.xmlvhy.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName LayUiResponse
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/10 12:54
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LayUiResponse {
    private Integer code;
    private String msg;
    private Long count;
    private Object data;

    /**
     *功能描述: 响应layui form 数据封装
     * @Author 小莫
     * @Date 12:56 2019/04/10
     * @Param [count, data]
     * @return com.xmlvhy.crawler.entity.LayUiResponse
     */
    public static LayUiResponse result(Long count,Object data){
        return new LayUiResponse(0,"",count,data);
    }
}
