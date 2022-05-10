package com.qdc.demoeurekaconsumer1.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration    //配置类，该类作用是获得RestTemplate类的对象
public class RibbonConfig {
    @Bean
    @LoadBalanced  // 负载均衡
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();  // 通过RestTemplateBuilder创建RestTemplate对象，调用服务提供者里面的方法
    }
}
