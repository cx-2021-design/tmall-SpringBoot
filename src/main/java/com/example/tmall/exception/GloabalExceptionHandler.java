package com.example.tmall.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;

/**
 * 作者：cjy
 * 类名：GloabalExceptionHandler
 * 全路径类名：com.example.tmall.exception.GloabalExceptionHandler
 * 父类或接口：
 * 描述：异常处理类（删除父类信息时，因为外键约束的存在而导致违反约束，产生异常。）
 */
public class GloabalExceptionHandler {

    /**
     * 方法名：defaultErrorHandler
     * 传入参数：@param req 请求事情
     *         @param e 异常
     * 异常类型：@throws ClassNotFoundException
     * 返回类型：@return {@link String }
     * 描述：异常处理器
     */
    @ExceptionHandler(value = Exception.class)
    public String defaultErrorHandler(HttpServletRequest req, Exception e) throws ClassNotFoundException {
        e.printStackTrace(); // 打印堆栈跟踪信息
        // 获取 org.hibernate.exception.ConstraintViolationException 异常类
        Class constraintViolationException = Class.forName("org.hibernate.exception.ConstraintViolationException");

        if(e.getCause()!= null && constraintViolationException==e.getCause().getClass()) {
            return "违反了外键约束";
        }
        return e.getMessage();
    }
}
