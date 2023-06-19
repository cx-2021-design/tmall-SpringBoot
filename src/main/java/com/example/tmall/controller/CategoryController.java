package com.example.tmall.controller;

import com.example.tmall.pojo.Category;
import com.example.tmall.service.CategoryService;
import com.example.tmall.util.ImageUtil;
import com.example.tmall.util.Page4Navigator;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 作者：cjy
 * 类名：CategoryController
 * 全路径类名：com.example.tmall.controller.CategoryController
 * 父类或接口：
 * 描述：分类控制类
 */
@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 方法名：list
     * 传入参数：@param start 开始
     *         @param size 大小
     * 异常类型：@throws Exception 异常
     * 返回类型：@return {@link Page4Navigator }<{@link Category }>
     * 描述：分页查询分类
     */
    @GetMapping("/categories")
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Page4Navigator<Category> page =categoryService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
    }
    /**
     * 添加分类信息并保存对应的图片文件。
     * @param bean 分类对象
     * @param image 图片文件
     * @param request HTTP请求对象
     * @return 添加成功的分类对象
     * @throws Exception 添加过程中发生的异常
     */
    @PostMapping("/categories")
    public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {
        // 调用categoryService的add方法添加分类信息
        categoryService.add(bean);
        // 调用saveOrUpdateImageFile方法保存或更新图片文件
        saveOrUpdateImageFile(bean, image, request);
        return bean;
    }

    /**
     * 保存或更新分类对应的图片文件。
     * @param bean 分类对象
     * @param image 图片文件
     * @param request HTTP请求对象
     * @throws IOException 保存过程中发生的I/O异常
     */
    public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
        // 获取存储图片文件的文件夹路径
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));

        // 构造图片文件对象，命名为分类对象的ID加上.jpg后缀
        File file = new File(imageFolder, bean.getId() + ".jpg");

        // 如果图片文件的父目录不存在，则创建父目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // 将上传的图片文件保存到目标文件
        image.transferTo(file);

        // 将图片文件转换为JPEG格式
        BufferedImage img = ImageUtil.change2jpg(file);

        // 将转换后的图片文件写入目标文件
        ImageIO.write(img, "jpg", file);
    }
    @DeleteMapping("/categories/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        categoryService.delete(id);
        File  imageFolder= new File(request.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return null;
    }
    @GetMapping("/categories/{id}")
    public Category get(@PathVariable("id") int id) throws Exception {
        Category bean=categoryService.get(id);
        return bean;
    }
    @PutMapping("/categories/{id}")
    public Object update(Category bean, MultipartFile image,HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        bean.setName(name);
        categoryService.update(bean);

        if(image!=null) {
            saveOrUpdateImageFile(bean, image, request);
        }
        return bean;
    }
}

