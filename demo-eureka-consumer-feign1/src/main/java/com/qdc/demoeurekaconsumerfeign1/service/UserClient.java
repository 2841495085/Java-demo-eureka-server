package com.qdc.demoeurekaconsumerfeign1.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;


// Feign 是springCould中另外一种实现负载均衡的方法，
// 其可以实现声明式的服务调用，在程序中可以实现像调用本地方法一样调用接口
// 实际上Feign 的底层也是使用Ribbon的，相当于对Ribbon的封装，因此Feign也天然的支持负载均衡。
@FeignClient(name = "eureka-provider1")
public interface UserClient {
//    利用Feign无需写具体的实现方法，会自动生成
    @RequestMapping(value = "/port")
    public String hello();
}

