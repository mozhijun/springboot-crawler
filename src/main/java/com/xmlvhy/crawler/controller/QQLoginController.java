package com.xmlvhy.crawler.controller;

import com.xmlvhy.crawler.config.QQConfig;
import com.xmlvhy.crawler.entity.Customer;
import com.xmlvhy.crawler.entity.ResponseResult;
import com.xmlvhy.crawler.service.CustomerService;
import com.xmlvhy.crawler.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QQLoginController
 * @Description TODO QQ授权登录控制器
 * @Author 小莫
 * @Date 2019/04/19 13:15
 * @Version 1.0
 **/
@Controller
@RequestMapping("/qq")
@Slf4j
public class QQLoginController {

    @Autowired
    private QQConfig qqConfig;

    @Autowired
    private CustomerService customerService;

    /**
     *功能描述: 进入qq授权登录页面
     * @Author 小莫
     * @Date 18:29 2019/04/19
     * @Param [state]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    @RequestMapping("oauth")
    @ResponseBody
    public ResponseResult oauthUrl(@RequestParam(value = "state",required = true) String state) throws UnsupportedEncodingException {
        String access_page = URLEncoder.encode(state,"utf-8");
        String callBackUrl = qqConfig.getRedirectUrl();

        String oauthUrl = String.format(qqConfig.getOauthUrl(), qqConfig.getAppId(), callBackUrl, access_page);

        Map<String,Object> ret = new HashMap<>();
        ret.put("oauthUrl",oauthUrl);
        return ResponseResult.success(ret);
    }

    /**
     *功能描述: qq回调
     * @Author 小莫
     * @Date 18:29 2019/04/19
     * @Param [code, state, session, response]
     * @return java.lang.String
     */
    @RequestMapping("login")
    public String callBack(@RequestParam("code") String code,
                         @RequestParam("state") String state,
                         HttpSession session, HttpServletResponse response){
        log.info("code: {},state :{}",code,state);
        Customer customer = customerService.saveQQCustomer(code);
        if (customer != null) {
            String token = JwtUtils.geneJsonWebToken(customer);
            session.setAttribute("token",token);
            //String redirectUrl = state + "?token=" + token;
            //log.info("QQ 授权登录成功进入：{}",redirectUrl);
            //try {
            //    response.sendRedirect(redirectUrl);
            //} catch (IOException e) {
            //    log.error("重定向失败，原因: {}",e.getMessage());
            //}
            //转到后台页面
            return "redirect:/manage/center"+"?token="+token;
        }
        //转到登录页面
        return "redirect:/";
    }
}
