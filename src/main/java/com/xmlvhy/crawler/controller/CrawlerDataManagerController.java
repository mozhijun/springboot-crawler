package com.xmlvhy.crawler.controller;

import com.github.pagehelper.Page;
import com.xmlvhy.crawler.entity.Content;
import com.xmlvhy.crawler.entity.Customer;
import com.xmlvhy.crawler.entity.LayUiResponse;
import com.xmlvhy.crawler.entity.ResponseResult;
import com.xmlvhy.crawler.service.ContentService;
import com.xmlvhy.crawler.service.CustomerService;
import com.xmlvhy.crawler.utils.CommonUtil;
import com.xmlvhy.crawler.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CrawlerDataManagerController
 * @Description TODO 数据管理后台
 * @Author 小莫
 * @Date 2019/04/09 21:24
 * @Version 1.0
 **/
@Controller
@Slf4j
@RequestMapping("/manage")
public class CrawlerDataManagerController {

    @Autowired
    private ContentService contentService;
    @Autowired
    private CustomerService customerService;

    @RequestMapping("/center")
    public String index(String token, Model model){
        Claims claims = JwtUtils.checkJWT(token);
        Integer customerId = (Integer) claims.get("id");
        Customer customer = customerService.getCustomerById(customerId);
        customer.setPassword(null);
        model.addAttribute("customer",customer);
        return "manage_center";
    }

    /**
     *功能描述: 展示所有爬取的数据
     * @Author 小莫
     * @Date 22:28 2019/04/09
     * @Param []
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> getContentList(){
        List<Map<String,Object>> contentList = contentService.getAllContents();
        Map result = new HashMap();
        if (contentList.size() > 0) {
            //layui对前台返回的数据是有格式要求的
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", contentList.size());
            result.put("data",contentList);
        }
        return result;
    }
    
    /**
     *功能描述: 分页获取数据
     * @Author 小莫
     * @Date 12:32 2019/04/10
     * @Param [page, limit] 
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @RequestMapping("list_by_page")
    @ResponseBody
    public Map<String,Object> findContentsByPage(@RequestParam(name = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(name = "limit",defaultValue = "10") Integer limit){
        Page<Map<String, Object>> pageList = contentService.getAllContentsByPage(page,limit);
        Map<String,Object> result = new HashMap<>();
        result.put("code",0);
        result.put("msg","");
        result.put("count",pageList.getTotal()); //Total 代表是不分页的情况下记录总数是多少
        result.put("data",pageList.getResult());//result属性包含了当前页的数据
        return result;
    }

    /**
     *功能描述: 根据条件获取分页数据
     * @Author 小莫
     * @Date 12:58 2019/04/10
     * @Param [page, limit, channelId, contentType, keyword]
     * @return com.xmlvhy.crawler.entity.LayUiResponse
     */
    @RequestMapping("list_by_params")
    @ResponseBody
    public LayUiResponse findContentByPageAndParams(@RequestParam(name = "page",defaultValue = "1") Integer page,
                                                    @RequestParam(name = "limit",defaultValue = "10") Integer limit,
                                                    Integer channelId, String contentType, String keyword){

        Page<Map<String, Object>> mapPage = contentService.getAllContentsByPageAndParams(page, limit, channelId,contentType,keyword);
        return LayUiResponse.result(mapPage.getTotal(),mapPage.getResult());
    }

    /**
     *功能描述: 删除一条内容
     * @Author 小莫
     * @Date 14:47 2019/04/10
     * @Param [contentId]
     * @return com.xmlvhy.crawler.entity.ResponseResult
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResponseResult deleteContent(Long contentId){
        if (contentService.removeContent(contentId)) {
            return ResponseResult.success("该条内容已被删除");
        }
        return ResponseResult.fail("内容删除失败");
    }

    /**
     *功能描述: 预览接口
     * @Author 小莫
     * @Date 15:21 2019/04/10
     * @Param [contentId]
     * @return org.springframework.web.servlet.ModelAndView
     */
    @RequestMapping("/preview/{contentId}.html")
    public ModelAndView preview(@PathVariable(name = "contentId") Long contentId){
        Content ret = contentService.findContentByContentId(contentId);
        if (ret == null) {
            log.warn("内容不存在");
            return null;
        }
        Map<String, Object> previewData = contentService.getPreviewData(contentId);
        Content content = (Content) previewData.get("content");
        String time = null;
        try {
            time = CommonUtil.convertTimeToFormat(content.getPasstime());
        } catch (ParseException e) {
            log.error("时间转化失败");
        }
        ModelAndView mv = new ModelAndView("preview");
        mv.addAllObjects(previewData);
        mv.addObject("time",time);
        return mv;
    }
}
