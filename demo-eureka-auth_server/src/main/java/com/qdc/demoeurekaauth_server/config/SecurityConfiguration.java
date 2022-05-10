package com.qdc.demoeurekaauth_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@EnableWebSecurity
@Component
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//    //用户详情服务
//    @Autowired
//    private UserDetailsService userDetailsService;

    // 密码编码格式
    @Autowired
    private PasswordEncoder passwordEncoder;
    // 使用BCrypt
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 授权方式提供者
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return authenticationProvider;
    }

    /**
     * 对静态资源不需要授权
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/fonts/**","/icon/**",
                "/favicon.ico");

    }

    /**
     * 用于对所有的http访问设置权限策略
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        // http.requestMatchers()方法用于对URL进行匹配，满足参数中指定模式的URL将会继续执行后面的子方法
        // authorizeRequests()用于请求授权
        // permitAll()用于指定URL可以被任何用户访问，而无需授权
        // authenticated()用于指定URL需要授权才可以访问
        // antMatchers()可以多次使用，它会按照使用的顺序去匹配URL，被前面antMatchers()匹配的URL将不会被传递到后面的antMatchers()

//     除了这些路径，其他的都需要授权令牌才可访问
//     对 /login,/login-error,/oauth/authorize,/oauth/token,/api/userinfo请求授权，
//     其中/login允许所有用户访问，而无需授权。
//     /login-error,/oauth/authorize,/oauth/token,/api/userinfo都需要通过身份认证才可以被访问。
        http.requestMatchers().antMatchers("/login","/login-error","/oauth/authorize",
                "/oauth/token","/api/userinfo").and().authorizeRequests()
                .antMatchers("/login").permitAll().anyRequest().authenticated();
        // 登录页面
        http.formLogin().loginPage("/login").failureUrl("/login-error");
        // 禁用CSRF
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        // 设置授权方式
        auth.authenticationProvider(authenticationProvider());
    }
}
