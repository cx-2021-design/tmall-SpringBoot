package com.example.tmall.comparator;

import com.example.tmall.pojo.Product;
import java.util.Comparator;

/**
 * 作者：cjy
 * 类名：ProductPriceComparator
 * 全路径类名：com.example.tmall.comparator.ProductPriceComparator
 * 父类或接口：@see Comparator
 * 描述：产品价格比较
 */
public class ProductPriceComparator implements Comparator<Product> {

    @Override
    public  int compare(Product p1, Product p2) {
        return  (p1.getPromotePrice().compareTo(p2.getPromotePrice()));
    }

}