package com.xmlvhy.crawler.controller;

import com.xmlvhy.crawler.config.WeChatConfig;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WeChatController
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/13 19:53
 * @Version 1.0
 **/
@Controller
@RequestMapping("/weChat")
@Slf4j
public class WeChatController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private WeChatConfig weChatConfig;

    /**
     *功能描述: 首先调出微信登录二维码扫一扫页面
     * @Author 小莫
     * @Date 20:07 2019/04/13
     * @Param [accessPage]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    @RequestMapping("login_url")
    @ResponseBody
    public ResponseResult getWeChatLoginQRCodeUrl(@RequestParam(value = "access_page", required = true) String accessPage) {
        //获取开放平台重定向地址
        String redirectUrl = weChatConfig.getRedirectUrl();
        Map<String,Object> ret = new HashMap<>();
        try {
            String callBackUrl = URLEncoder.encode(redirectUrl, "GBK");
            //TODO:可以直接格式化替换 对应%s的参数
            String qrodeUrl = String.format(weChatConfig.getOpenQRCodeUrl(), weChatConfig.getAppId(), callBackUrl, accessPage);
            ret.put("qrCodeUrl",qrodeUrl);
            return ResponseResult.success(ret);
        } catch (UnsupportedEncodingException e) {
            log.info("回调地址编码失败：{}", e.getMessage());
            return ResponseResult.fail("调出微信扫码登录url失败,请重新尝试");
        }
    }

    @RequestMapping("callback")
    public void weChatCallBack(@RequestParam(value = "code",required = true) String code,
                               String state, HttpServletResponse response,
                               HttpSession session){
        log.info("code: {} , state: {}",code,state);
        Customer customer = customerService.saveWeChatCustomer(code);
        if (customer != null) {
            //生成jwt
            String token = JwtUtils.geneJsonWebToken(customer);
            session.setAttribute("token",token);

            //state 参数表示当前授权登录后要登入的网页url,需要拼接 http:// 这样才不会页面内跳转
            String redirectUrl = state + "?token="+token;
            log.info("redirectUrl: {}",redirectUrl);
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                log.info("重定向失败，原因：{}",e.getMessage());
            }
        }
    }

}
