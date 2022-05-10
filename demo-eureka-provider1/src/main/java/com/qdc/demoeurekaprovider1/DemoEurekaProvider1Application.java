package com.qdc.demoeurekaprovider1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableEurekaClient  //服务实例
@SpringBootApplication
//@EnableResourceServer   //设置当前程序为一个集成安全机制的服务提供者程序
public class DemoEurekaProvider1Application {

    public static void main(String[] args) {
        SpringApplication.run(DemoEurekaProvider1Application.class, args);
    }

}
