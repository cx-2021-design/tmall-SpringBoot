package com.example.tmall.comparator;

import com.example.tmall.pojo.Product;
import java.util.Comparator;

/**
 * 作者：cjy
 * 类名：ProductReviewComparator
 * 全路径类名：com.example.tmall.comparator.ProductReviewComparator
 * 父类或接口：@see Comparator
 * 描述：产品评论数量比较器
 */
public class ProductReviewComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount()-p1.getReviewCount();
    }

}
