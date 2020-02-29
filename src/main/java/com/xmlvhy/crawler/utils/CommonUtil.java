package com.xmlvhy.crawler.utils;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName CommonUtil
 * @Description TODO 通用工具类
 * @Author 小莫
 * @Date 2019/04/10 17:00
 * @Version 1.0
 **/
public class CommonUtil {
    /**
     *功能描述: 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     * @Author 小莫
     * @Date 17:00 2019/04/10
     * @Param [d]
     * @return java.lang.String
     */
    public static String convertTimeToFormat (String timeString) throws ParseException {
        //将时间字符串转化为日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = dateFormat.parse(timeString);
        //将时间转化为毫秒来计算
        long curTime = System.currentTimeMillis() / (long) 1000;
        long timeStamp = d.getTime()/1000l;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    /**
     * md5常用工具类
     * @param data
     * @return
     */
    public static String MD5(String data){
        try {

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte [] array = md5.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) {
        String pwd = "123456";
        String s = MD5(pwd);
        System.out.println("CommonUtil.main "+s);
    }

}
