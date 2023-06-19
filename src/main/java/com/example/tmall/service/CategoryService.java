package com.example.tmall.service;

import com.example.tmall.dao.CategoryDAO;
import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.Product;
import com.example.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 作者：cjy
 * 类名：CategoryService
 * 全路径类名：com.example.tmall.service.CategoryService
 * 父类或接口：
 * 描述：分类的服务类
 */
@Service
@CacheConfig(cacheNames="categories")
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;

    /**
     * 方法名：list
     * 传入参数：
     * 异常类型：
     * 返回类型：@return {@link List }<{@link Category }>
     * 描述：按id排序列出所有分类
     */
    @Cacheable(key="'categories-all'")
    public List<Category> list(){
        Sort sort = new Sort(Sort.Direction.DESC,"id"); // 构建排序条件
        return categoryDAO.findAll(sort); // 查询所有分类并按照排序条件返回
    }
    /**
     * 根据起始位置、每页数量和导航页码数量获取分页后的 Category 列表。
     * @param start 起始位置
     * @param size 每页数量
     * @param navigatePages 导航页码数量
     * @return 分页后的 Category 列表
     */
    @Cacheable(key="'categories-page-'+#p0+ '-' + #p1")
    public Page4Navigator<Category> list(int start, int size, int navigatePages) {
        // 指定排序方式为按照 id 降序排列
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        // 创建分页请求对象
        Pageable pageable = new PageRequest(start, size, sort);
        // 调用 categoryDAO 的 findAll 方法进行分页查询
        Page pageFromJPA = categoryDAO.findAll(pageable);
        // 将 JPA 的 Page 对象转换为 Page4Navigator 对象并返回
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }
    @CacheEvict(allEntries=true)//在方法执行后，将清除指定的缓存中的所有条目
    public void add(Category bean) {
        categoryDAO.save(bean);
    }

    @CacheEvict(allEntries=true)//在方法执行后，将清除指定的缓存中的所有条目
    public void delete(int id) {
        categoryDAO.delete(id);
    }

    @Cacheable(key="'categories-one-'+ #p0")
    public Category get(int id) {
        Category c= categoryDAO.findById(id);
        return c;
    }
    @CacheEvict(allEntries=true)
    public void update(Category bean) {
        categoryDAO.save(bean);
    }
    public void removeCategoryFromProduct(List<Category> cs) {
        for (Category category : cs) {
            removeCategoryFromProduct(category);
        }
    }
    //删除Product对象上的 分类,防止序列化时导致无穷循环
    public void removeCategoryFromProduct(Category category) {
        List<Product> products =category.getProducts();
        if(null!=products) {
            for (Product product : products) {
                product.setCategory(null);
            }
        }

        List<List<Product>> productsByRow =category.getProductsByRow();
        if(null!=productsByRow) {
            for (List<Product> ps : productsByRow) {
                for (Product p: ps) {
                    p.setCategory(null);
                }
            }
        }
    }
}

