package com.example.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：cjy
 * 类名：Category
 * 全路径类名：com.example.tmall.pojo.Category
 * 父类或接口：@see Serializable
 * 描述：产品的分类
 */
@Entity
@Table(name = "category")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"}) // 避免在将实体类转换为json格式时产生错误
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String name;

    /**
     * 描述：同一分类下的产品
     */
    @Transient
    List<Product> products;
    /**
     * 描述：同一分类下的一行行产品
     */
    @Transient
    List<List<Product>> productsByRow;
}

