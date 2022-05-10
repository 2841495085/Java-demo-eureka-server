package com.qdc.demoeurekazuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * IP地址过滤器
 */
@Component
public class IPFilter extends ZuulFilter {

    private String[] whitelist;   // 白名单列表

    @Value("${yxwfilter.ip.whitelist}")
    private String strIPWhitelist;

    @Value("${yxwfilter.ip.whitelistenabled}")
    private String WhitelistEnabled;   // 是否启用白名单，利用value注解去与配置文件进行联系


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        if ("true".equalsIgnoreCase(WhitelistEnabled)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 在 run()方法中，程序首先拆分白名单IP地址列表；然后调用this.getIpAddr(req)方法以返回
     * 请求保重所包含的IP地址，判断其是否在白名单中。如果不在，则调用ctx.setResponseStatusCode(401)方法，
     * 返回401，代表没有访问权限
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println(strIPWhitelist);
        whitelist = strIPWhitelist.split("\\,");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        String ipAddr = this.getIpAddr(req);
        System.out.println("请求IP地址为 ：[" + ipAddr + "]");  //配置本地IP白名单，生产环境可放入数据库或者redis中

        // 这里定义一个集合，为的是将数组里的IP地址放入集合中，然后调用集合的contains()方法进行判断集合中是否有当前的IP地址
        List<String> ips = new ArrayList<>();
        for (int i = 0; i < whitelist.length; i++){
            System.out.println(whitelist[i]);   // 这里输出abc
            ips.add(whitelist[i]);  // 遍历数组添加到集合ips中
        }

        System.out.println("whitelist ：" + ips.toString());  //配置本地IP白名单，生产环境可放入数据库或者redis中

        if (!ips.contains(ipAddr)){   // 调用集合的contains()方法进行判断，白名单里是否存下当前IP地址
            System.out.println("未通过IP地址校验，[" + ipAddr + "]");
            ctx.setResponseStatusCode(401);
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            ctx.setResponseBody("{\"errrocode\":\"00001\", \"errmsg\": \"IpAddr is forbidden![" + ipAddr + "]\"}");
        }

        return null;
    }

    /**
     * 获取客户端ip地址
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");   // 真实的IP地址
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");   // 客户端代理IP地址
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");  // 集群代理IP地址
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();  // 用于获取请求客户端的 IP 地址。
        }
        return ip;
    }
}
