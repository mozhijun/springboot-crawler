package com.xmlvhy.crawler.interceptors;

import com.xmlvhy.crawler.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName LoginInterceptor
 * @Description TODO 登录拦截器
 * @Author 小莫
 * @Date 2019/04/12 18:48
 * @Version 1.0
 **/
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //String token = request.getHeader("token");
        //if (token == null) {
        //    token = request.getParameter("token");
        //}
        String token = (String) request.getSession().getAttribute("token");
        if (token != null) {
            Claims claims = JwtUtils.checkJWT(token);
            if (claims != null) {
                return true;
            }
        }
        //用户信息不存在，则拦截，重定向到后台登陆界面
        response.sendRedirect("/crawler");
        return false;
    }
}
