package com.qdc.demoeurekaconsumer1.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    // 依赖restTemplate对象
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @RequestMapping(value = "users")
    public String testalluser(){
        return restTemplate.getForObject("http://EUREKA-PROVIDER1/user/userall",String.class);
    }

    @RequestMapping(value = "userdetails/{userid}")
    public String testuserdetails(@PathVariable(value = "userid") String id){
        return restTemplate.getForObject("http://EUREKA-PROVIDER1/user/details?userid="+id,String.class);
    }

    /*@RequestMapping(value = "addUser")
    @ResponseBody
    public ResponseEntity<String> testaddUser(@RequestBody User user){
        return restTemplate.postForObject("http://EUREKA-PROVIDER1/user/add",user,String.class);
    }*/

    @RequestMapping(value = "/port")
    public String testPort(){
        return restTemplate.getForObject("http://EUREKA-PROVIDER1/port",String.class);
    }

    @GetMapping("/sayHi")
//    fallbackMethod指定了当Hystrix打开时所执行的服务降级机制，执行下面的sayHiFallback方法
//    @HystrixCommand注解指定Hystrix的属性
//    例如:在调用服务User的接口/user/sayHi时，为了防止服务User出现异常，可以在调用接口的相关函数中
//         使用@HystrixCommand注解来启用熔断器
    @HystrixCommand(fallbackMethod = "sayHiFallback",commandProperties = {
//            服务熔断时间
//            @HystrixProperty注解：用于指定HystrixCommand命令参数的name和value
//          execution.isolation.thread.timeoutInMilliseconds参数
//          该参数用于配置HystrixCommand.run()执行的超时时间，单位为ms。如果执行命令的时间超过该值且设置启用超时时间，将启用熔断器
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "30000")
    })
    public String hello(@RequestParam(value = "sleep_seconds") int sleep_seconds) throws InterruptedException{
        ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-provider1");
        String url = "http://" + serviceInstance.getHost() + ":" +serviceInstance.getPort() +
                "/user/sayHi?sleep_seconds=" + sleep_seconds;
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url,String.class);


    }

//    定义熔断器执行的方法
    public String sayHiFallback(int sleep_seconds){
        return "服务User展示无法响应，请稍候.......";
    }



}
