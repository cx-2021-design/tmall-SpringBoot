package com.example.tmall.controller;

import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.ProductImage;
import com.example.tmall.service.CategoryService;
import com.example.tmall.service.ProductImageService;
import com.example.tmall.service.ProductService;
import com.example.tmall.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductImageController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@RequestParam("type") String type, @PathVariable("pid") int pid) throws Exception {
        // 获取指定pid的Product对象
        Product product = productService.get(pid);

        if(ProductImageService.type_single.equals(type)) {
            // 获取单个类型的ProductImage列表
            List<ProductImage> singles =  productImageService.listSingleProductImages(product);
            return singles;
        }
        else if(ProductImageService.type_detail.equals(type)) {
            // 获取详情类型的ProductImage列表
            List<ProductImage> details =  productImageService.listDetailProductImages(product);
            return details;
        }
        else {
            // 返回空列表
            return new ArrayList<>();
        }
    }


    @PostMapping("/productImages")
    public Object add(@RequestParam("pid") int pid, @RequestParam("type") String type, MultipartFile image, HttpServletRequest request) throws Exception {
        // 创建一个新的ProductImage对象
        ProductImage bean = new ProductImage();
        // 获取指定pid的Product对象
        Product product = productService.get(pid);
        // 设置ProductImage的关联产品和类型
        bean.setProduct(product);
        bean.setType(type);

        // 将ProductImage对象保存到数据库
        productImageService.add(bean);

        // 处理上传的图片文件
        String folder = "img/";
        if(ProductImageService.type_single.equals(bean.getType())){
            folder +="productSingle";
        }
        else{
            folder +="productDetail";
        }
        // 获取保存图片的文件夹路径
        File imageFolder= new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            // 将上传的图片文件保存到磁盘
            image.transferTo(file);
            // 将图片转换为JPG格式并保存
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ProductImageService.type_single.equals(bean.getType())){
            // 处理单个类型的ProductImage的缩略图
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.getParentFile().mkdirs();
            f_middle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file, 56, 56, f_small);
            ImageUtil.resizeImage(file, 217, 190, f_middle);
        }

        return bean;
    }

    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        // 获取指定id的ProductImage对象
        ProductImage bean = productImageService.get(id);
        // 从数据库中删除指定的ProductImage对象
        productImageService.delete(id);

        // 删除保存在磁盘上的图片文件
        String folder = "img/";
        if(ProductImageService.type_single.equals(bean.getType()))
            folder +="productSingle";
        else
            folder +="productDetail";

        File  imageFolder= new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder,bean.getId()+".jpg");
        String fileName = file.getName();
        file.delete();
        if(ProductImageService.type_single.equals(bean.getType())){
            // 同时删除单个类型ProductImage的缩略图
            String imageFolder_small= request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getServletContext().getRealPath("img/productSingle_middle");
            File f_small = new File(imageFolder_small, fileName);
            File f_middle = new File(imageFolder_middle, fileName);
            f_small.delete();
            f_middle.delete();
        }

        return null;
    }


}