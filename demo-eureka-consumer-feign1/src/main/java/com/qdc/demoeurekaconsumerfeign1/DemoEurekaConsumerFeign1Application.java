package com.qdc.demoeurekaconsumerfeign1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableDiscoveryClient  //  用于启用服务发现功能
@SpringBootApplication
@EnableFeignClients   //  指定项目自动扫描所有的@FeignClient注解
public class DemoEurekaConsumerFeign1Application {

    public static void main(String[] args) {
        SpringApplication.run(DemoEurekaConsumerFeign1Application.class, args);
    }

}
