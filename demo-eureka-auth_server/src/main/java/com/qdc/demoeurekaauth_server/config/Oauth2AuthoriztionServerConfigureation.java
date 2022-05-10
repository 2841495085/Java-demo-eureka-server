package com.qdc.demoeurekaauth_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Configuration
@Component
// 用于实现OAuth 2.0认证服务的配置
public class Oauth2AuthoriztionServerConfigureation extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;  //userDetailsService对象：用于对终端用户进行身份认证

    @Autowired
    private DataSource druidDataSource;  //druidDataSource对象：用于根据配置连接数据库

    /**
     * 用来配置令牌端点（token endpoint）的安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 这对于access token认证很重要，只要具有ROLE_TRUSTED_CLIENT权限的客户端才可以通过认证
        // 所以要将表oauth_client_details中客户端记录的authorities字段设置成ROLE_TRUSTED_CLIENT，
        // 这样才能使客户端满足条件
        security.checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");
    }

    /**
     * 用来指定客户端应用的管理方法
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 数据库管理客户端应用，从dataSource配置的数据源中读取客户端数据
        // 客户端数据都保存在表oauth_client_details中
        clients.withClientDetails(new JdbcClientDetailsService(druidDataSource));

    }

    /**
     * 用来配置授权以及access token的访问端点和令牌服务（生成token）
     * @param endpoints
     * @throws Exception
     */
    // 配置认证服务器的非安全属性，总之一切都通过数据库管理
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 用户信息查询服务
        endpoints.userDetailsService(userDetailsService);
        // 数据库管理access token和refresh token
        TokenStore tokenStore = new JdbcTokenStore(druidDataSource);
//        endpoints.tokenStore(tokenStore);

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(new JdbcClientDetailsService(druidDataSource));
        tokenServices.setAccessTokenValiditySeconds(38000); // 设置令牌有效时间
        tokenServices.setRefreshTokenValiditySeconds(180);  // 设置令牌刷新时间
        endpoints.tokenServices(tokenServices);
        //数据库管理授权码
        endpoints.authorizationCodeServices(new JdbcAuthorizationCodeServices(druidDataSource));
        //数据库管理授权信息
        ApprovalStore approvalStore = new JdbcApprovalStore(druidDataSource);
        endpoints.approvalStore(approvalStore);


    }
}
