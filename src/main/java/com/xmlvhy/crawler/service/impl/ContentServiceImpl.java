package com.xmlvhy.crawler.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xmlvhy.crawler.dao.*;
import com.xmlvhy.crawler.entity.Content;
import com.xmlvhy.crawler.entity.Forum;
import com.xmlvhy.crawler.entity.User;
import com.xmlvhy.crawler.exception.CrawlerException;
import com.xmlvhy.crawler.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ContentServiceImpl
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/10 10:14
 * @Version 1.0
 **/
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ForumMapper forumMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 功能描述: 查询所有内容，这里由于content表中涉及到用户表user以及频道表channel
     * 实体类中映射的非两个表的对象而是主键，这里使用一个小技巧就是，多表联查，得到的结果使用
     * map来封装，这样查询到的数据，mybatis会自动帮我们映射属性值
     *
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author 小莫
     * @Date 10:18 2019/04/10
     * @Param []
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Map<String, Object>> getAllContents() {
        List<Map<String, Object>> mapList = contentMapper.selectAll();
        return mapList;
    }


    /**
     * 功能描述: 查询分页
     *
     * @return com.github.pagehelper.Page<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author 小莫
     * @Date 10:27 2019/04/10
     * @Param [page, pageSize]
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Page<Map<String, Object>> getAllContentsByPage(Integer page, Integer pageSize) {
        //pageHelper中会自动帮我生成limit分页语句
        //以及自动帮我们执行查询总数count
        PageHelper.startPage(page, pageSize);
        Page<Map<String, Object>> pageList = (Page<Map<String, Object>>) contentMapper.selectAll();
        return pageList;
    }

    /**
     * 功能描述: 根据条件查询并且将数据分页
     *
     * @return com.github.pagehelper.Page<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author 小莫
     * @Date 12:51 2019/04/10
     * @Param [page, limit, params]
     */
    @Override
    public Page<Map<String, Object>> getAllContentsByPageAndParams(Integer page, Integer limit,
                                                                   Integer channelId,
                                                                   String contentType, String keyword) {
        Map<String, Object> params = new HashMap<>();
        if (channelId != null && channelId != -1) {
            params.put("channelId", channelId);
        }
        if (contentType != null && !contentType.equals("-1")) {
            params.put("contentType", contentType);
        }
        if (keyword != null && !keyword.trim().equals("")) {
            params.put("keyword", "%" + keyword + "%");
        }

        PageHelper.startPage(page, limit);
        Page<Map<String, Object>> pageList = (Page<Map<String, Object>>) contentMapper.selectByParams(params);
        return pageList;
    }

    /**
     * 功能描述: 移除某一条内容
     *
     * @return java.lang.Boolean
     * @Author 小莫
     * @Date 14:39 2019/04/10
     * @Param [contentId]
     */
    @Override
    public Boolean removeContent(Long contentId) {
        int rows = contentMapper.deleteByPrimaryKey(contentId);
        if (rows >= 1) {
            return true;
        }
        return false;
    }

    /**
     * 功能描述: 获取预览的所有数据
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author 小莫
     * @Date 15:19 2019/04/10
     * @Param [contentId]
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Map<String, Object> getPreviewData(Long contentId) {
        //先拿到内容
        Content content = contentMapper.selectByPrimaryKey(contentId);
        //获取用户信息
        User user = userMapper.selectByPrimaryKey(content.getUid());
        //获取帖子内容
        Forum forum = null;
        //有的内容是没有圈子的
        if (content.getForumId() != null) {
            forum = forumMapper.selectByPrimaryKey(content.getForumId());
        }
        //获取评论内容
        List<Map<String, Object>> commonList = commentMapper.selectByContentId(contentId);

        //所有结果使用map来封装预览的数据
        Map<String, Object> prevData = new HashMap<>();
        prevData.put("content", content);
        prevData.put("commons", commonList);
        prevData.put("forum", forum);
        prevData.put("user", user);

        if (content.getContentType().equalsIgnoreCase("video")) {
            List<Map<String, Object>> videoList = videoMapper.selectByContentId(contentId);
            prevData.put("video", videoList.get(0));
        } else if (content.getContentType().equalsIgnoreCase("image") ||
                content.getContentType().equalsIgnoreCase("gif")) {
            List<Map<String, Object>> imageList = imageMapper.selectByContentId(contentId);
            prevData.put("image", imageList.get(0));
        }
        return prevData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Content findContentByContentId(Long contentId) {
        Content content = contentMapper.selectByPrimaryKey(contentId);
        if (content == null) {
            throw new CrawlerException("内容不存在");
        }
        return content;
    }
}
