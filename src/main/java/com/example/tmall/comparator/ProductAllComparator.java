package com.example.tmall.comparator;

import com.example.tmall.pojo.Product;
import java.util.Comparator;

/**
 * 作者：cjy
 * 类名：ProductAllComparator
 * 全路径类名：com.example.tmall.comparator.ProductAllComparator
 * 父类或接口：@see Comparator
 * 描述：产品总体比较器
 */
public class ProductAllComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount()*p2.getSaleCount()-p1.getReviewCount()*p1.getSaleCount();
    }

}