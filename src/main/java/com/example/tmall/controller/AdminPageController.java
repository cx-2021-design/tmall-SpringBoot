package com.example.tmall.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 作者：cjy
 * 类名：AdminPageController
 * 全路径类名：com.example.tmall.controller.AdminPageController
 * 父类或接口：
 * 描述：后台管理页面的跳转控制类
 */
@Controller
@Slf4j
public class AdminPageController {
    /**
     * 方法名：admin
     * 传入参数：
     * 异常类型：
     * 返回类型：@return {@link String }
     * 描述：访问地址 /admin，跳转到 admin_category_list 方法
     */
    @GetMapping(value = "/admin")
    public String admin() {
        return "redirect:admin_category_list"; // 跳转到方法(重定向到方法)
    }

    /**
     * 方法名：listCategory
     * 传入参数：
     * 异常类型：
     * 返回类型：@return {@link String }
     * 描述： 访问 admin_category_list ，跳转到 admin/listCategory.html
     */
    @GetMapping(value = "admin_category_list")
    public String listCategory() {
        return "admin/listCategory"; // 跳转到页面（返回页面）
    }

    @GetMapping(value="/admin_category_edit")
    public String editCategory(){
        return "admin/editCategory";

    }

    @GetMapping(value="/admin_order_list")
    public String listOrder(){
        return "admin/listOrder";

    }

    @GetMapping(value="/admin_product_list")
    public String listProduct(){
        return "admin/listProduct";

    }

    @GetMapping(value="/admin_product_edit")
    public String editProduct(){
        return "admin/editProduct";

    }
    @GetMapping(value="/admin_productImage_list")
    public String listProductImage(){
        return "admin/listProductImage";

    }

    @GetMapping(value="/admin_property_list")
    public String listProperty(){
        return "admin/listProperty";

    }

    @GetMapping(value="/admin_property_edit")
    public String editProperty(){
        return "admin/editProperty";

    }

    @GetMapping(value="/admin_propertyValue_edit")
    public String editPropertyValue(){
        return "admin/editPropertyValue";

    }

    @GetMapping(value="/admin_user_list")
    public String listUser(){
        return "admin/listUser";

    }
}








