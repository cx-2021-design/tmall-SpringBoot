package com.example.tmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 作者：cjy
 * 类名：CORSConfiguration
 * 全路径类名：com.example.tmall.config.CORSConfiguration
 * 父类或接口：@see WebMvcConfigurerAdapter
 * 描述：允许所有请求跨域
 */
@Configuration
public class CORSConfiguration extends WebMvcConfigurerAdapter{
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //所有请求都允许跨域
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
