package com.xmlvhy.crawler.controller;

import com.xmlvhy.crawler.entity.Customer;
import com.xmlvhy.crawler.entity.ResponseResult;
import com.xmlvhy.crawler.service.CustomerService;
import com.xmlvhy.crawler.utils.CommonUtil;
import com.xmlvhy.crawler.utils.JwtUtils;
import com.xmlvhy.crawler.vo.CustomerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/12 15:20
 * @Version 1.0
 **/
@Controller
public class LoginController {

    @Autowired
    private CustomerService customerService;

    /**
     *功能描述: 登录页面
     * @Author 小莫
     * @Date 20:45 2019/04/12
     * @Param []
     * @return java.lang.String
     */
    @RequestMapping("/")
    public String index(){
        return "login";
    }

    /**
     *功能描述: 确认登录
     * @Author 小莫
     * @Date 20:45 2019/04/12
     * @Param [username, password, code, session]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    @RequestMapping("login")
    @ResponseBody
    public ResponseResult login(String username, String password,
                                String code, HttpSession session){

        String RandCode = (String) session.getAttribute("code");

        //校验验证码
        if (!RandCode.equalsIgnoreCase(code)) {
            return ResponseResult.fail("验证码不正确");
        }

        Customer customer = customerService.checkPasswordAndLoginName(username, password);
        Map token = new HashMap();
        if (customer != null) {
            String ret = JwtUtils.geneJsonWebToken(customer);
            token.put("token",ret);
            session.setAttribute("token",ret);
            return ResponseResult.success("登录成功！",token);
        }
        return ResponseResult.fail("用户名或密码不正确");
    }

    /**
     *功能描述: 退出登录
     * @Author 小莫
     * @Date 20:44 2019/04/12
     * @Param [session]
     * @return java.lang.String
     */
    @RequestMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value = "register",method = RequestMethod.GET)
    public String register(){
        return "register";
    }

    /**
     *功能描述: 注册
     * @Author 小莫
     * @Date 22:00 2019/04/12
     * @Param [customerVo]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult register(CustomerVo customerVo){
        //检查loginName 是否存在
        String loginName = customerVo.getLoginName();
        Customer customer = customerService.getCustomerByLoginName(loginName);
        if (customer != null) {
            return ResponseResult.fail("该用户名已存在!");
        }
        //加密密码
       String password = CommonUtil.MD5(customerVo.getPassword());
       customerVo.setPassword(password);
        if (customerService.saveCustomer(customerVo)) {
            return ResponseResult.success("恭喜您，注册成功！");
        }
        return ResponseResult.fail("注册失败");
    }
}
