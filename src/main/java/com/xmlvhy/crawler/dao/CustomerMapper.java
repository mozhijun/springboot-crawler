package com.xmlvhy.crawler.dao;

import com.xmlvhy.crawler.entity.Customer;
import org.apache.ibatis.annotations.Param;

public interface CustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    Customer selectByPwdAndLoginName(@Param("loginName") String username,
                                     @Param("password") String password);

    Customer selectCustomerByLoginName(String loginName);

    Customer selectCustomerByOpenId(String openid);
}