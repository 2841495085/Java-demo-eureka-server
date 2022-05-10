package com.qdc.demoeurekazuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

public class MyPreFilter1 extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";  // 过滤器的类型
    }

    @Override
    public int filterOrder() {
        return 0;  // 返回一个int的值定义过滤器执行顺序，数值越小，优先级越高
    }

    @Override
    public boolean shouldFilter() {
        return true;  //指定Zuul过滤器的条件，过滤器是否使用；false为默认不使用，当需要使用是改为 true
    }


    // 指定当 shouldFilter()方法返回true时Zuul过滤器执行的操作
    @Override
    public Object run() throws ZuulException {
        System.out.println("MyPreFilter1");
        return null;
    }
}
