package com.qdc.demoeurekaconsumer1.utils;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 该类用于获取 access token
 * 将数据转化成 BasicAuth
 */
public class HttpClientWithBasicAuth {

    public HttpClientWithBasicAuth() {}

    /**
     * 手动构造 Basic Auth头信息
     * @return
     */
    private String getHeader(String UserName, String Password){
//      auth由两部分组成：Username和Password组成
        String auth = UserName + ":" + Password;
//      使用Base64将auth进行加密，auth.getBytes()转换成二进制，Charset.forName("US-ASCII")，利用US-ASCII进行编码
        byte[] encodeAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
//        注意Basic后面有一个空格 ，因为其控制台显示的 authHeader为 Basic dGVzdDoxMjM0NTY=
        String authHeader = "Basic " + new String(encodeAuth);
//        System.out.println(authHeader);
        return authHeader;

    }


    /**
     *  向指定的认证服务发送请求，以获取access_token
     * @param url: 认证服务的 RUL
     * @param UserName: 应用程序的 Client id
     * @param Password: 应用程序的 Client Secret
     * @param params: 包含 grant_type和 scope 的参数列表
     * @return
     */
    public String send(String url, String UserName, String Password, Map<String,String> params){
        //  实例化一个post对象，HttpPost对象映射一的是post方法调用接口
        HttpPost post = new HttpPost(url);

        CloseableHttpClient client = HttpClients.createDefault();

        //  组织请求参数，在获取 access token时参数为 grant_type和 scope
        //  对Map集合进行遍历，Map集合中存入的是key:value的键值对
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0){
            Set<String> keySet = params.keySet();
            for (String key : keySet){
                paramList.add(new BasicNameValuePair(key,params.get(key)));
            }
        }

        try {
            // 调用setEntity（）方法设置要提交的数据，将paramList参数转换为UTF-8格式进行提交。
            post.setEntity(new UrlEncodedFormEntity(paramList,"utf-8"));
        }catch (UnsupportedEncodingException e1){
            e1.printStackTrace();
        }
        // 在设置的请求中将Baisc Auth信息添加到Post请求包头中。
        post.addHeader("Authorization",getHeader(UserName,Password));
        // post.setEntity(myEntity);     // 设置请求体
        String responseContent = null;   // 响应内容
        CloseableHttpResponse response = null;

        try {
            // 执行提交请求，execute执行post请求，并对返回的结果进行判断。
            response = client.execute(post);
            // System.out.println(JSON.toJSONString(response));
            int status_code = response.getStatusLine().getStatusCode();  //获取响应的执行码
            System.out.println("status_code:" + status_code);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
            }
             System.out.println("responseContent:" + responseContent);

        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (response != null){
                    response.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    if (client != null){
                        client.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return responseContent;

    }

}
