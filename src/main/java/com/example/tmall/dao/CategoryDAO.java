package com.example.tmall.dao;

import com.example.tmall.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 作者：cjy
 * 类名：CategoryDAO
 * 全路径类名：com.example.tmall.dao.CategoryDAO
 * 父类或接口：@see JpaRepository
 * 描述：分类DAO层
 */
public interface CategoryDAO extends JpaRepository<Category,Integer> {
    Category findById(int id);
}
