package com.example.tmall.config;

import com.example.tmall.interceptor.LoginInterceptor;
import com.example.tmall.interceptor.OtherInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 作者：cjy
 * 类名：WebMvcConfigurer
 * 全路径类名：com.example.tmall.config.WebMvcConfigurer
 * 父类或接口：@see WebMvcConfigurerAdapter
 * 描述：登录注册拦截器
 */
@Configuration
class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    /**
     * 创建 LoginInterceptor 实例的 Bean
     */
    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }
    @Bean
    public OtherInterceptor getOtherIntercepter() {
        return new OtherInterceptor();
    }

    /**
     * 添加拦截器到拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor())
                .addPathPatterns("/**");//将拦截器应用于所有的请求路径
        registry.addInterceptor(getOtherIntercepter())
                .addPathPatterns("/**");
    }
}
