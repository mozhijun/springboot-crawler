package com.xmlvhy.crawler.service.impl;

import com.xmlvhy.crawler.config.QQConfig;
import com.xmlvhy.crawler.config.WeChatConfig;
import com.xmlvhy.crawler.dao.CustomerMapper;
import com.xmlvhy.crawler.entity.Customer;
import com.xmlvhy.crawler.exception.CrawlerException;
import com.xmlvhy.crawler.service.CustomerService;
import com.xmlvhy.crawler.utils.CommonUtil;
import com.xmlvhy.crawler.utils.HttpUtils;
import com.xmlvhy.crawler.utils.OgnlUtil;
import com.xmlvhy.crawler.utils.QQHttpClientUtil;
import com.xmlvhy.crawler.vo.CustomerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName CustomerServiceImpl
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/12 17:47
 * @Version 1.0
 **/
@Service
@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private QQConfig qqConfig;

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Customer checkPasswordAndLoginName(String username, String password) {
        password = CommonUtil.MD5(password);
        Customer customer = customerMapper.selectByPwdAndLoginName(username, password);
        if (customer == null) {
            throw new CrawlerException("用户名或密码错误");
        }
        return customer;
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Customer getCustomerById(Integer id) {
        return customerMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean saveCustomer(CustomerVo customerVo) {
        Customer customer = new Customer();
        String sex = customerVo.getSex();
        String gender = null;
        if ("1".equals(sex)) {
            gender = "男";
        }else if("2".equals(sex)){
            gender = "女";
        }else{
            gender = "保密";
        }
        BeanUtils.copyProperties(customerVo,customer);
        customer.setSex(gender);
        customer.setCreateTime(new Date());

        int rows = customerMapper.insert(customer);
        if (rows >=1) {
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Customer getCustomerByLoginName(String loginName) {
        return customerMapper.selectCustomerByLoginName(loginName);
    }

    /**
     *功能描述: 保存使用微信登录的用户信息
     * @Author 小莫
     * @Date 22:04 2019/04/13
     * @Param [code]
     * @return com.xmlvhy.crawler.entity.Customer
     */
    @Override
    public Customer saveWeChatCustomer(String code) {
        //定义获取accessToken的url
        String accessTokenUrl = String.format(weChatConfig.getAccessTokenUrl(),
                weChatConfig.getAppId(),weChatConfig.getAppSecret(),code);
        //通过code拿到accessToken
        Map<String, Object> resultMap = HttpUtils.doGet(accessTokenUrl);
        if (resultMap == null || resultMap.isEmpty()) {
            return null;
        }

        //获取accessToken
        String access_token = OgnlUtil.getString("access_token", resultMap);
        String openid = OgnlUtil.getString("openid", resultMap);

        //首先根据 openid 查询此用户是否存在
        Customer customer = customerMapper.selectCustomerByOpenId(openid);
        if (customer != null) {
            return customer;
        }

        //通过openid 和 accessToken 拿到微信用户的信息
        String userInfoUrl = String.format(weChatConfig.getUserInfoUrl(),access_token,openid);
        Map<String, Object> userInfoMap = HttpUtils.doGet(userInfoUrl);

        String nickname = OgnlUtil.getString("nickname", userInfoMap);
        int sex = OgnlUtil.getNumber("sex", userInfoMap).intValue();
        String headimgurl = OgnlUtil.getString("headimgurl", userInfoMap);

        Customer ct = new Customer();
        ct.setSex(sex == 1 ? "男" : "女");
        ct.setHeader(headimgurl);
        ct.setName(nickname);
        ct.setUid(openid);
        ct.setCreateTime(new Date());

        int rows = customerMapper.insert(ct);
        if (rows >= 1) {
            return ct;
        }
        return null;
    }

    /**
     *功能描述: 保存qq用户登录的信息
     * @Author 小莫
     * @Date 13:54 2019/04/19
     * @Param [code]
     * @return com.xmlvhy.crawler.entity.Customer
     */
    @Override
    public Customer saveQQCustomer(String code) {

        //1.首先获取 access token
        String accessTokenUrl = String.format(qqConfig.getAccessTokenUrl(),qqConfig.getAppId(),qqConfig.getAppKey(),code,qqConfig.getRedirectUrl());
        String accessToken  = null;
        try {
            accessToken = QQHttpClientUtil.getAccessToken(accessTokenUrl);
            log.info("获取的token信息：{}",accessToken);
        } catch (IOException e) {
            log.error("获取token失败，原因：{}",e.getMessage());
        }
        //2.通过access_token拿到openid
        String openIdUrl = String.format(qqConfig.getOpenIdUrl(),accessToken);
        Map<String, Object> openidResultMap = QQHttpClientUtil.doGet(openIdUrl);
        log.info("获取用户的openID信息：{}",openidResultMap);
        String openid = OgnlUtil.getString("openid", openidResultMap);

        //查看该用户是否已经有过记录
        Customer ret = customerMapper.selectCustomerByOpenId(openid);
        if (ret != null) {
            return ret;
        }

        //3.通过 access_token、openId和appid获取用户信息
        String userInfoUrl = String.format(qqConfig.getUserInfoUrl(), accessToken, qqConfig.getAppId(), openid);
        Map<String, Object> userInfoMap = HttpUtils.doGet(userInfoUrl);
        log.info("获取的用户信息：{}",userInfoMap);

        String nickname = OgnlUtil.getString("nickname", userInfoMap);
        String figureurl = OgnlUtil.getString("figureurl", userInfoMap);
        String gender = OgnlUtil.getString("gender", userInfoMap);

        //将用户信息保存到数据库中
        Customer customer = new Customer();
        customer.setUid(openid);
        customer.setName(nickname);
        customer.setHeader(figureurl);
        customer.setSex(gender);
        customer.setCreateTime(new Date());

        int rows = customerMapper.insert(customer);
        if (rows >= 1) {
            return customer;
        }
        return null;
    }
}
