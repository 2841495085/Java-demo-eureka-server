//package com.qdc.demoeurekaprovider1.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//
//@Configuration
//public class Oauth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
////  声明认证access_token的URL
//    private static final String URL="http://localhost:3333/oauth/check_token";
//    // 运行完这句话以后，会跳到Eureka-auth-server里的Oauth2AuthoriztionServerConfigureation方法，
//    // 利用里面的security对象调用checkTokenAccess()方法检查是否具有访问权限
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
////        设置验证Token的方法，即使用test和123456的客户端身份去URL验证token
//        RemoteTokenServices tokenServices = new RemoteTokenServices();
//
//        tokenServices.setCheckTokenEndpointUrl(URL);
////        设置认证的客户端的id和密码
//        tokenServices.setClientId("test");
//        tokenServices.setClientSecret("123456");
//
//        resources.tokenServices(tokenServices);
////        设置当前资源服务器的 Resource Id为 userall
////        注意当前的Client Id是否拥有对Resource Id的访问权限
//        resources.resourceId("userall").stateless(true);
//
//
//    }
//
//}
