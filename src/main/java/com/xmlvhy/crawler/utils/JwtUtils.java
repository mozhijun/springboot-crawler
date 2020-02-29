package com.xmlvhy.crawler.utils;

import com.xmlvhy.crawler.constant.CommonConstants;
import com.xmlvhy.crawler.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Author: 小莫
 * Date: 2019-02-14 15:06
 * Description:jwt校验工具类
 */
public class JwtUtils {

    /**
     *功能描述: 生成 jwt token
     * @Author 小莫
     * @Date 15:45 2019/02/14
     * @Param [user] 
     * @return java.lang.String
     */
    public static String geneJsonWebToken(Customer customer){
        if (customer == null) {
            return null;
        }
        String token = Jwts.builder().setSubject(CommonConstants.JWT_SUBJECT)
                .claim("id", customer.getId())
                .claim("name", customer.getName())
                .claim("img", customer.getHeader())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + CommonConstants.JWT_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, CommonConstants.JWT_APPSECRET).compact();
        return token;
    }
    
    /**
     *功能描述: 校验token
     * @Author 小莫
     * @Date 15:51 2019/02/14
     * @Param [token] 
     * @return io.jsonwebtoken.Claims
     */
    public static Claims checkJWT(String token){
        try {
            final Claims claims = Jwts.parser().setSigningKey(CommonConstants.JWT_APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        }catch (Exception e){
            return null;
        }
    }
}
