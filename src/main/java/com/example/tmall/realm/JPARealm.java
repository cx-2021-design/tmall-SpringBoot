package com.example.tmall.realm;

import com.example.tmall.pojo.User;
import com.example.tmall.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class JPARealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 创建一个SimpleAuthorizationInfo对象用于存储授权信息
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();

        // 在这里可以添加角色和权限信息
        // s.addRole("role1");
        // s.addRole("role2");
        // s.addStringPermission("permission1");
        // s.addStringPermission("permission2");

        // 返回授权信息对象
        return s;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)throws AuthenticationException {
        // 获取用户名
        String userName = token.getPrincipal().toString();

        // 根据用户名从userService中获取用户信息
        User user = userService.getByName(userName);

        // 从数据库中获取存储的密码和盐值
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();

        // 创建SimpleAuthenticationInfo对象用于存储认证信息，包括用户名、密码和盐值
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userName,
                passwordInDB,
                ByteSource.Util.bytes(salt),
                getName());

        // 返回认证信息对象
        return authenticationInfo;
    }



}
