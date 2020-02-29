package com.xmlvhy.crawler.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xmlvhy.crawler.constant.CommonConstants;
import com.xmlvhy.crawler.dao.*;
import com.xmlvhy.crawler.entity.*;
import com.xmlvhy.crawler.service.DataProcessedService;
import com.xmlvhy.crawler.utils.OgnlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DataProcessedServiceImpl
 * @Description TODO
 * @Author 小莫
 * @Date 2019/04/09 18:39
 * @Version 1.0
 **/
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
@Slf4j
public class DataProcessedServiceImpl implements DataProcessedService {

    @Autowired
    private SourceMapper sourceMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ForumMapper forumMapper;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public void sourceDataEtl() {
        Gson gson = new Gson();
        //首先拿到所有未处理的源数据
        List<Source> sourceList = sourceMapper.findAllByState(CommonConstants.SOURCE_STATE_WAIT);
        for (Source source : sourceList) {
            String resultContent = source.getResponseText();
            //将json字符串转化为map对象
            Map<String, Object> rootMap = gson.fromJson(resultContent, new TypeToken<Map<String, Object>>() {
            }.getType());
            //获取每页的数据集
            List<Map<String, Object>> mapList = OgnlUtil.getListMap("list", rootMap);
            //处理每一条数据
            int i = 0;
            for (Map<String, Object> contentMap : mapList) {
                i++;
                createContent(source, contentMap);
            }
            //对处理完成的Source对象更新状态为Processed
            source.setState(CommonConstants.SOURCE_STATE_PROCESS);
            int upSourceRow = sourceMapper.updateByPrimaryKey(source);
            if (upSourceRow == 0) {
                log.error("数据源更新status失败");
            }
        }
    }

    /**
     * 功能描述: 将每一条数据处理并保存到数据库中
     *
     * @return void
     * @Author 小莫
     * @Date 19:21 2019/04/09
     * @Param [source, contentMap]
     */
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public void createContent(Source source, Map<String, Object> contentMap) {
        Long contentId = OgnlUtil.getNumber("id", contentMap).longValue();
        //首先查询该content 是否已经存在，以为每个模块的数据有可能存在相同的内容，例如排行模块可能与任何模块都有重合的数据
        Content ret = contentMapper.selectByPrimaryKey(contentId);
        if (ret != null) {
            log.info("contentId= {} ,内容已经存在，无须重复导入，被忽略", contentId);
            return;
        }

        //先保存content
        Content content = new Content();
        /*频道id*/
        content.setChannelId(source.getChannelId());
        /*帖子状态 4 表示通过审核*/
        content.setStatus(OgnlUtil.getNumber("status", contentMap).intValue());
        /*收藏数量*/
        content.setBookmarkCount(OgnlUtil.getNumber("bookmark", contentMap).intValue());
        /*评论数量*/
        content.setCommentCount(OgnlUtil.getNumber("comment", contentMap).intValue());
        /*点赞数量*/
        content.setLikeCount(OgnlUtil.getNumber("up", contentMap).intValue());
        /*踩的数量*/
        content.setHateCount(OgnlUtil.getNumber("down", contentMap).intValue());
        /*内容编号*/
        content.setContentId(OgnlUtil.getNumber("id", contentMap).longValue());
        /*标题或者正文内容*/
        content.setContentText(OgnlUtil.getString("text", contentMap));
        /*内容类型*/
        content.setContentType(OgnlUtil.getString("type", contentMap));
        /*过审时间*/
        content.setPasstime(OgnlUtil.getString("passtime", contentMap));
        /*分享数量*/
        content.setShareCount(OgnlUtil.getNumber("forward", contentMap).intValue());
        /*分享url*/
        content.setShareUrl(OgnlUtil.getString("share_url", contentMap));
        /*数据源id*/
        content.setSourceId(source.getSourceId());
        /*创建时间*/
        content.setCreateTime(new Date());

        //保存content
        int rows = contentMapper.insert(content);
        if (rows == 0) {
            log.error("content 数据导入失败");
        }
        //分别提取视频 图片 以及 笑话到对应数据模块
        if (content.getContentType().equalsIgnoreCase("video")) {
            Video video = new Video();
            /*内容编号*/
            video.setContentId(content.getContentId());
            /*视频下载url*/
            List<String> videoDownUrlList = OgnlUtil.getListString("video.download", contentMap);
            video.setDownloadUrl(videoDownUrlList.size() > 0 ? videoDownUrlList.get(0) : null);
            /*视频播放时长*/
            video.setDuration(OgnlUtil.getNumber("video.duration", contentMap).intValue());
            /*视频高度*/
            video.setHeight(OgnlUtil.getNumber("video.height", contentMap).intValue());
            /*视频宽度*/
            video.setWidth(OgnlUtil.getNumber("video.width", contentMap).intValue());
            /*视频播放数量*/
            video.setPlaycount(OgnlUtil.getNumber("video.playcount", contentMap).intValue());
            video.setPlayfcount(OgnlUtil.getNumber("video.playfcount", contentMap).intValue());
            /*视频缩略图 原尺寸*/
            List<String> thumUrlList = OgnlUtil.getListString("video.thumbnail", contentMap);
            video.setThumb(thumUrlList.size() > 0 ? thumUrlList.get(0) : null);

            /*视频缩略图 正比*/
            List<String> thumSmallUrlList = OgnlUtil.getListString("video.thumbnail_small", contentMap);
            video.setThumb(thumSmallUrlList.size() > 0 ? thumSmallUrlList.get(0) : null);

            /*视频url*/
            List<String> videoUrlList = OgnlUtil.getListString("video.video", contentMap);
            video.setVideoUrl(videoUrlList.size() > 0 ? videoUrlList.get(0) : null);

            //保存video内容
            int videoRow = videoMapper.insert(video);
            if (videoRow == 0) {
                log.info("video 内容导入失败");
            }
        } else if (content.getContentType().equalsIgnoreCase("image")) {
            //处理图片数据
            Image image = new Image();
            //原图url
            List<String> bigUrl = OgnlUtil.getListString("image.big", contentMap);
            image.setBigUrl(bigUrl.size() > 0 ? bigUrl.get(0) : null);
            /*内容编号*/
            image.setContentId(content.getContentId());
            /*图片高度*/
            image.setRawHeight(OgnlUtil.getNumber("image.height", contentMap).intValue());
            /*图片宽度*/
            image.setRawWidth(OgnlUtil.getNumber("image.width", contentMap).intValue());
            /*图片下载url*/
            List<String> downUrl = OgnlUtil.getListString("image.download_url", contentMap);
            image.setWatermarkerUrl(downUrl.size() > 0 ? downUrl.get(0) : null);
            /*缩略图url*/
            List<String> thumbUrl = OgnlUtil.getListString("image.thumbnail_small", contentMap);
            image.setThumbUrl(thumbUrl.size() > 0 ? thumbUrl.get(0) : null);

            //保存image
            int imageRow = imageMapper.insert(image);
            if (imageRow == 0) {
                log.error("image 数据导入失败");
            }
        }else if(content.getContentType().equalsIgnoreCase("gif")){
            //保存 gif
            Image gif = new Image();
            /*原图url*/
            List<String> bigUrl = OgnlUtil.getListString("gif.images", contentMap);
            gif.setBigUrl(bigUrl.size() > 0 ? bigUrl.get(0) : null);
            /*高度*/
            gif.setRawHeight(OgnlUtil.getNumber("gif.height", contentMap).intValue());
            /*宽度*/
            gif.setRawWidth(OgnlUtil.getNumber("gif.width", contentMap).intValue());
            /*水印图片*/
            List<String> downloadUrl = OgnlUtil.getListString("gif.download_url", contentMap);
            gif.setWatermarkerUrl(downloadUrl.size() > 0 ? downloadUrl.get(0) : null);
            /*缩略图*/
            List<String> thumb = OgnlUtil.getListString("gif.thumbnail_small", contentMap);
            gif.setThumbUrl(thumb.size() > 0 ? thumb.get(0) : null);
            /*内容编号*/
            gif.setContentId(content.getContentId());

            int gifRow = imageMapper.insert(gif);
            if (gifRow == 0) {
                log.error("gif 数据导入失败");
            }
        }

        //保存用户信息，先查询用户信息是否已存在，如不存在则创建
        Number uid = OgnlUtil.getNumber("u.uid", contentMap);
        /*判断用户id是否为空，小概率数据会没有uid*/
        if (uid != null) {
            User user = userMapper.selectByPrimaryKey(uid.longValue());
            if (user == null) {
                //用户不存在则创建
                user = new User();
                /*用户头像url*/
                List<String> headUrl = OgnlUtil.getListString("u.header", contentMap);
                user.setHeader(headUrl.size() > 0 ? headUrl.get(0) : null);
                /*是否 v*/
                user.setIsV(OgnlUtil.getBoolean("u.is_v",contentMap) ? 1 : 0);
                /*是否是vip*/
                user.setIsVip(OgnlUtil.getBoolean("u.is_vip",contentMap) ? 1 : 0);
                /*用户昵称*/
                user.setNickname(OgnlUtil.getString("u.name",contentMap));
                /*用户房间图片*/
                user.setRoomIcon(OgnlUtil.getString("u.room_icon",contentMap));
                /*用户房间名称*/
                user.setRoomName(OgnlUtil.getString("u.room_name",contentMap));
                /*用户房间角色*/
                user.setRoomRole(OgnlUtil.getString("u.room_role",contentMap));
                /*用户房间url*/
                user.setRoomUrl(OgnlUtil.getString("u.room_url",contentMap));
                /*用户id*/
                user.setUid(uid.longValue());
                int userRow = userMapper.insert(user);
                if (userRow == 0) {
                    log.error("user 数据导入失败");
                }
                //content 保存uid,也就是更新一下
                content.setUid(user.getUid());
                int contentRowUp = contentMapper.updateByPrimaryKey(content);
                if (contentRowUp == 0) {
                    log.error("content 更新 uid 失败");
                }
            }
        }

        //处理所有评论信息
        List<Map<String, Object>> topComments = OgnlUtil.getListMap("top_comments", contentMap);
        if (topComments != null) {
            //遍历保存每一个评论信息
            for(Map<String,Object> commentMap : topComments){
                Comment comment = new Comment();
                //评论对象
                comment.setCommentId(OgnlUtil.getNumber("id" , commentMap).longValue());
                //评论内容
                comment.setCommentText(OgnlUtil.getString("content", commentMap));
                //过审时间
                comment.setPasstime(OgnlUtil.getString("passtime", commentMap));

                //配置评论区的用户,有则加载,无则创建
                Long commentUid = OgnlUtil.getNumber("u.uid", commentMap).longValue();
                //查询该用户是否已导入
                User user = userMapper.selectByPrimaryKey(commentUid);
                //有则加载，无则创建
                if (user == null) {
                    user = new User();
                    List<String> headerUrl = OgnlUtil.getListString("u.header", commentMap);
                    user.setHeader(headerUrl.size() > 0 ? headerUrl.get(0) : null);
                    user.setUid(OgnlUtil.getNumber("u.uid", commentMap).longValue());
                    user.setIsVip(OgnlUtil.getBoolean("u.is_vip", commentMap) ? 1 : 0);
                    user.setRoomUrl(OgnlUtil.getString("u.room_url", commentMap));
                    user.setRoomName(OgnlUtil.getString("u.room_name", commentMap));
                    user.setRoomRole(OgnlUtil.getString("u.room_role", commentMap));
                    user.setRoomIcon(OgnlUtil.getString("u.room_icon", commentMap));
                    user.setNickname(OgnlUtil.getString("u.name", commentMap));
                    int commentUserRow = userMapper.insert(user);
                    if (commentUserRow == 0) {
                        log.error("评论部分的用户数据导入失败");
                    }
                }
                //设置用户编号
                comment.setUid(user.getUid());
                //设置内容编号
                comment.setContentId(content.getContentId());
                //插入数据
                int commentRow = commentMapper.insert(comment);
                if (commentRow == 0) {
                    log.error("comment 数据导入失败");
                }
            }
        }

        //更新圈子内容
        List<Map<String, Object>> tags = OgnlUtil.getListMap("tags", contentMap);
        //只获取第一个论坛
        if (tags.size() > 0) {
            Map<String, Object> tag = tags.get(0);
            Long forumId = OgnlUtil.getNumber("id", tags.get(0)).longValue();
            Forum forum = forumMapper.selectByPrimaryKey(forumId);
            //查找论坛,有则加载,无则创建
            if (forum == null) {
                forum = new Forum();
                //帖子数量
                forum.setPostCount(OgnlUtil.getNumber("post_number", tag).intValue());
                //logo地址
                forum.setLogo(OgnlUtil.getString("image_list", tag));
                //排序
                forum.setForumSort(OgnlUtil.getNumber("forum_sort", tag).intValue());
                //论坛状态
                forum.setForumStatus(OgnlUtil.getNumber("forum_status", tag).intValue());
                //论坛编号
                forum.setForumId(forumId);
                //论坛信息
                forum.setInfo(OgnlUtil.getString("info", tag));
                //论坛名称
                forum.setName(OgnlUtil.getString("name", tag));
                //用户总量
                forum.setUserCount(OgnlUtil.getNumber("sub_number", tag).intValue());
                //保存数据
                int forumRow = forumMapper.insert(forum);
                if (forumRow == 0) {
                    log.error("forum 数据导入失败");
                }
            }
            //更新内容，保存圈子编号
            content.setForumId(forum.getForumId());
            int contentForumUpRow = contentMapper.updateByPrimaryKey(content);
            if (contentForumUpRow == 0) {
                log.error("content保存圈子编号失败");
            }
        }
        log.info("Content ID:{} ，所有内容成功导入" , contentId);
    }
}
