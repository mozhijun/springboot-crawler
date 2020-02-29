package com.xmlvhy.crawler.utils;

import lombok.extern.slf4j.Slf4j;
import ognl.Ognl;
import ognl.OgnlException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OgnlUtil
 * @Description TODO: 对象图导航工具类
 * @Author 小莫
 * @Date 2019/04/09 18:14
 * @Version 1.0
 **/
@Slf4j
public class OgnlUtil {

    /**
     *功能描述: 获取字符串
     * @Author 小莫
     * @Date 18:17 2019/04/09
     * @Param [ognl, rootMap]
     * @return java.lang.String
     */
    public static String getString(String ognl, Map<String,Object> rootMap){
        try {
            return Ognl.getValue(ognl, rootMap).toString();
        } catch (OgnlException e) {
            log.error("OGNL 获取字符串失败");
            throw new RuntimeException(e);
        }
    }

    /**
     *功能描述: 获取Number字段，Number是所有数字的父类
     * @Author 小莫
     * @Date 18:20 2019/04/09
     * @Param [ognl, rootMap]
     * @return java.lang.Number
     */
    public static Number getNumber(String ognl, Map<String,Object> rootMap){
        Number result = null;
        try {
            Object value = Ognl.getValue(ognl, rootMap);
            if (value != null) {
                if (value instanceof Number) {
                    result = (Number) value;
                }else if(value instanceof String){
                    //转化为数字
                    result = new BigDecimal((String) value);
                }
            }
        } catch (OgnlException e) {
            log.error("OGNL获取数字失败");
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     *功能描述: 获取 boolean
     * @Author 小莫
     * @Date 18:27 2019/04/09
     * @Param [ognl, root]
     * @return java.lang.Boolean
     */
    public static Boolean getBoolean(String ognl , Map root){
        Boolean result = null;
        try {
            Object val = Ognl.getValue(ognl , root);
            if(val != null){
                if(val instanceof  Boolean){
                    result = (Boolean) val;
                }else if(val instanceof  String){
                    result = ((String)val).equalsIgnoreCase("true")?true:false;
                }else if(val instanceof Number){
                    if(((Number)val).intValue() == 1){
                        result = true;
                    }else{
                        result = false;
                    }
                }
            }
        } catch (OgnlException e) {
            log.error("OGNL 获取Boolean失败");
            throw new RuntimeException(e);
        }
        return  result;
    }

    /**
     * 获取List集合，里面每一个元素都是Map
     * @param ognl
     * @param root
     * @return
     */
    public static List<Map<String,Object>> getListMap (String ognl , Map root){
        List<Map<String,Object>> listMap = null;
        try {
            listMap = (List) Ognl.getValue(ognl , root);
            if(listMap == null){
                listMap = new ArrayList();
            }

        } catch (OgnlException e) {
            log.error("获取 list Map失败");
            throw new RuntimeException(e);
        }
        return listMap;
    }

    /**
     * 获取list集合，里面每一个元素都是String
     * @param ognl
     * @param root
     * @return
     */
    public static List<String> getListString(String ognl , Map root){
        List<String> list = null;
        try {
            list = (List)Ognl.getValue(ognl , root);
            if(list == null){
                list = new ArrayList();
            }
        } catch (OgnlException e) {
            log.error("OGNL 获取list string失败");
            throw new RuntimeException(e);
        }
        return list;
    }

}
