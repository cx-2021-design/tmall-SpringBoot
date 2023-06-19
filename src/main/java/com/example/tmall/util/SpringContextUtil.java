package com.example.tmall.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 作者：cjy
 * 类名：SpringContextUtil
 * 全路径类名：com.example.tmall.util.SpringContextUtil
 * 父类或接口：@see ApplicationContextAware
 * 描述：用于获取 Spring 应用程序上下文的工具类。
 *      通过实现 ApplicationContextAware 接口，它可以获取并保存 Spring 应用程序上下文。
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * 私有构造函数，防止实例化
     */
    private SpringContextUtil() {

    }

    /**
     * Spring 应用程序上下文对象
     */
    private static ApplicationContext applicationContext;

    /**
     * 实现 ApplicationContextAware 接口的方法，
     * 在 Spring 容器启动时将应用程序上下文注入到该工具类的静态变量 applicationContext 中。
     *
     * @param applicationContext Spring 应用程序上下文对象
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取指定类型的 Bean 实例
     *
     * @param clazz Bean 的类型
     * @param <T>   Bean 的类型
     * @return Bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
