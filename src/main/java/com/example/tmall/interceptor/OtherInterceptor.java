package com.example.tmall.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.pojo.User;
import com.example.tmall.service.CategoryService;
import com.example.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class OtherInterceptor implements HandlerInterceptor {
    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderItemService orderItemService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");
        int cartTotalItemNumber = 0;

        // 如果用户已登录，则计算购物车中的总商品数量
        if (user != null) {
            List<OrderItem> orderItems = orderItemService.listByUser(user);
            for (OrderItem orderItem : orderItems) {
                cartTotalItemNumber += orderItem.getNumber();
            }
        }

        // 获取商品分类列表和上下文路径
        List<Category> categories = categoryService.list();
        String contextPath = httpServletRequest.getServletContext().getContextPath();

        // 将分类列表、购物车总商品数量和上下文路径存储到相应的作用域中
        httpServletRequest.getServletContext().setAttribute("categories_below_search", categories);
        session.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
        httpServletRequest.getServletContext().setAttribute("contextPath", contextPath);
    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}