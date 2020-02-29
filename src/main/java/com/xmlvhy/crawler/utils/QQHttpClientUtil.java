package com.xmlvhy.crawler.utils;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QQHttpClientUtil
 * @Description TODO : QQ授权登录相关接口，httpclient 请求
 * @Author 小莫
 * @Date 2019/04/19 15:04
 * @Version 1.0
 **/
public class QQHttpClientUtil {

    private static Gson gson = new Gson();

    /**
     *功能描述: 由于qq返回的结果，不是json,需要修改一下：
     * ( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
     * @Author 小莫
     * @Date 15:25 2019/04/19
     * @Param [result]
     * @return com.alibaba.fastjson.JSONObject
     */
    private static String parseJSONStr(String result){
        //截取 括号内的内容才是标准的json格式内容
        int startIndex = result.indexOf("(");
        int endIndex = result.lastIndexOf(")");
        String ret = result.substring(startIndex + 1, endIndex);
        return ret;
    }

    /**
     * 获取 Access_Token
     * @param url
     * @return
     * @throws IOException
     */
    public static String getAccessToken(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String token = null;

        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();

        //这里注意，请求accessToken，qq返回的如下数据格式，需要单独解析：
        // access_token=YOUR_ACCESS_TOKEN&expires_in=3600

        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            if (result.indexOf("access_token") >= 0) {
                String[] array = result.split("&");
                for (String str: array)
                    if (str.indexOf("access_token") >= 0) {
                        token = str.substring(str.indexOf("=") + 1);
                        break;
                    }
            }
        }

        httpGet.releaseConnection();
        return token;
    }

    /**
     * 获取 OpenID
     * @param url
     * @return
     * @throws IOException
     */
    //public static String getOpenID(String url) throws IOException {
    //    JSONObject jsonObject = null;
    //    CloseableHttpClient client = HttpClients.createDefault();
    //
    //    HttpGet httpGet = new HttpGet(url);
    //    HttpResponse response = client.execute(httpGet);
    //    HttpEntity entity = response.getEntity();
    //
    //    if (entity != null) {
    //        String result = EntityUtils.toString(entity, "UTF-8");
    //        jsonObject = parseJSONP(result);
    //    }
    //
    //    httpGet.releaseConnection();
    //    if (jsonObject != null)
    //        return jsonObject.getString("openid");
    //    else
    //        return null;
    //}

    /**
     * 获取用户信息
     * @param url
     * @return
     * @throws IOException
     */
    //public static JSONObject getUserInfo(String url) throws IOException {
    //    JSONObject jsonObject = null;
    //    CloseableHttpClient client = HttpClients.createDefault();
    //
    //    HttpGet httpGet = new HttpGet(url);
    //    HttpResponse response = client.execute(httpGet);
    //    HttpEntity entity = response.getEntity();
    //
    //    if (entity != null) {
    //        String result = EntityUtils.toString(entity, "UTF-8");
    //        jsonObject = JSONObject.parseObject(result);
    //    }
    //
    //    httpGet.releaseConnection();
    //    return jsonObject;
    //}

    /**
     *功能描述: QQ 请求获取 openid userInfo 等接口
     * @Author 小莫
     * @Date 15:29 2019/04/19
     * @Param [url]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    public static Map<String,Object> doGet(String url){

        Map<String,Object> map = new HashMap<>();
        CloseableHttpClient httpClient =  HttpClients.createDefault();
        //设置参数
        RequestConfig requestConfig =  RequestConfig.custom().setConnectTimeout(5000) //连接超时
                .setConnectionRequestTimeout(4000)//请求超时
                .setSocketTimeout(4000)
                .setRedirectsEnabled(true)  //允许自动重定向
                .build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        try{
            // 获取请求响应结果
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                String result = EntityUtils.toString(httpResponse.getEntity());
                //解决乱码
                result = new String(parseJSONStr(result).getBytes("ISO-8859-1"), "UTF-8");

                map = gson.fromJson(result,map.getClass());
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return map;
    }
}
