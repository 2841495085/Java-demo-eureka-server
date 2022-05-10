package com.qdc.demoeurekazuul.config;

import com.qdc.demoeurekazuul.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器的执行顺序为 MyPreFilter1、MyPreFilter2、MyRoutingFilter1、MyRoutingFilter2
 * MyPreFilter1和 MyPreFilter2都是Pre过滤器，MyPreFilter1优先被执行，这是因为它的 filterOrder()方法的返回值比较小
 * MyRoutingFilter2没有被执行，这是因为他的shouldFilter()方法返回false
 * 在执行完MyRoutingFilter1以后，程序通过ribbon调用 UserService
 */
@Configuration
public class FilterConfig {

    @Bean
    public MyPreFilter1 myPreFilter1(){
        return new MyPreFilter1();
    }

    @Bean
    public MyPreFilter2 myPreFilter2(){
        return new MyPreFilter2();
    }

    @Bean
    public MyRoutingFilter1 myRoutingFilter1(){
        return new MyRoutingFilter1();
    }

    @Bean
    public MyRoutingFilter2 myRoutingFilter2(){
        return new MyRoutingFilter2();
    }

    @Bean
    public MyErrorFilter myErrorFilter(){
        return new MyErrorFilter();
    }

    @Bean
    public MyPostFilter myPostFilter(){
        return new MyPostFilter();
    }
}
