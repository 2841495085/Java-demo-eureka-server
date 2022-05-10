package com.qdc.demoeurekaauth_server.service.impl;

import com.qdc.demoeurekaauth_server.pojo.Role;
import com.qdc.demoeurekaauth_server.pojo.User;
import com.qdc.demoeurekaauth_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// UserDetailsService是Spring Security组件中定义的接口，
// Spring Security组件利用它来验证用户名、密码和授权。
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

//    loadUserByUsername()方法：根据用户名查询用户详情，返回UserDetalis对象(带有权限信息的User对象)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUser(username);
        if(user == null || user.getId() < 1){
            throw new UsernameNotFoundException("Username not found: " + username);
        }
        /**
         * 权限验证代码
         * 将pojo里面的user对象转换成userdetails里面的user对象
         *
         *  查询成功，用户存储，需要匹配用户名和密码是否正确
         *  匹配密码，是由SpringSercurity内部逻辑自动完成，只需要把查询的用户名正确密码返回即可
         *  返回结果，是由UserDetails接口实现类型 User ,构造的时候需要提供7个参数
         *  用户名、密码、已启用账户、账户是否过期、账户是否过期、账户凭证是否被锁定、登陆用户权限集合
         *
         *  7个参数：
         *     username:用户名
         *     password:密码
         *     enable:账户是否启用
         *     accountNonExpired:账户是否过期
         *     credentialsNonExpire:账户凭证是否过期
         *     accountNonLocked:账户是否被锁定
         *     getGrantedAuthorities(user)：登录用户的权限集合
         */
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),true,true,
                true, true, getGrantedAuthorities(user)
        );
    }

    /**
     *  getGrantedAuthorities()方法:
     *      根据User对象获取用户权限数据，返回 Set<GrantedAuthority>对象，这就是用户权限数据的集合
     */
    private Collection<? extends GrantedAuthority> getGrantedAuthorities(User user){
//     由于数据库中没有role的信息，为了能够测试，new出一个role的对象添加到set集合中，然后添加到user中。
        Set<Role> roles = new HashSet<>();
        Role role1 = new Role(1,"admin");
        roles.add(role1);
        user.setRoles(roles);

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authorities;
    }

}
