package com.qdc.demoeurekaprovider2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortController {

//    使用@Value注解的Sp表达式，将全局配置文件application.yml中的端口号注入到PortController类中。
//    用来观察Ribbon进行了负载均衡
    @Value("${server.port}")
    String port;

    @RequestMapping(value = "port")
    public String testPort(){
        return "Hello ,I'm from provider2 ,and port: "+port;
    }




}
