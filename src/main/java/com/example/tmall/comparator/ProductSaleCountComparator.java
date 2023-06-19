package com.example.tmall.comparator;

import com.example.tmall.pojo.Product;
import java.util.Comparator;

/**
 * 作者：cjy
 * 类名：ProductSaleCountComparator
 * 全路径类名：com.example.tmall.comparator.ProductSaleCountComparator
 * 父类或接口：@see Comparator
 * 描述：产品销售数量比较
 */
public class ProductSaleCountComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount()-p1.getSaleCount();
    }

}