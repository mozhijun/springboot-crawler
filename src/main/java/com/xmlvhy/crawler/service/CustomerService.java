package com.xmlvhy.crawler.service;

import com.xmlvhy.crawler.entity.Customer;
import com.xmlvhy.crawler.vo.CustomerVo;

/**
 * @ClassName CustomerService
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/12 17:45
 * @Version 1.0
 **/
public interface CustomerService {

    Customer checkPasswordAndLoginName(String username,String password);

    Customer getCustomerById(Integer id);

    Boolean saveCustomer(CustomerVo customerVo);

    Customer getCustomerByLoginName(String loginName);

    Customer saveWeChatCustomer(String code);

    Customer saveQQCustomer(String code);
}
