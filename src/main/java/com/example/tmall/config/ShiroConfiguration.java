package com.example.tmall.config;

import com.example.tmall.realm.JPARealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 作者：cjy
 * 类名：ShiroConfiguration
 * 全路径类名：com.example.tmall.config.ShiroConfiguration
 * 父类或接口：
 * 描述：shiro配置
 */
@Configuration
public class ShiroConfiguration {
    /**
     * 获取LifecycleBeanPostProcessor实例，用于管理Shiro组件的生命周期
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 配置ShiroFilterFactoryBean，用于创建Shiro过滤器链
     * @param securityManager 安全管理器实例
     * @return ShiroFilterFactoryBean实例
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        return shiroFilterFactoryBean;
    }

    /**
     * 配置SecurityManager，用于管理Shiro的安全策略
     * @return SecurityManager实例
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(getJPARealm());
        return securityManager;
    }

    /**
     * 配置自定义的JPARealm，用于实现基于JPA的用户认证和授权
     * @return JPARealm实例
     */
    @Bean
    public JPARealm getJPARealm(){
        JPARealm myShiroRealm = new JPARealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * 配置HashedCredentialsMatcher，用于对密码进行散列加密和验证
     * @return HashedCredentialsMatcher实例
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);//md5为非对称加密算法
        hashedCredentialsMatcher.setHashIterations(100);
        return hashedCredentialsMatcher;
    }

    /**
     * 开启Shiro AOP注解支持，使用代理方式，需要开启代码支持
     * @param securityManager 安全管理器实例
     * @return AuthorizationAttributeSourceAdvisor实例
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}

