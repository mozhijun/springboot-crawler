package com.xmlvhy.crawler.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CustomerVo
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/12 21:47
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVo {
    private String loginName;
    private String password;
    private String name;
    private String sex;
}
