package com.example.tmall.dao;

import com.example.tmall.pojo.Category;
import com.example.tmall.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyDAO extends JpaRepository<Property,Integer> {
        Property findById(int id);
        Page<Property> findByCategory(Category category, Pageable pageable);
        List<Property> findByCategory(Category category);
}
