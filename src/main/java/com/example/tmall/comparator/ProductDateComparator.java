package com.example.tmall.comparator;

import com.example.tmall.pojo.Product;
import java.util.Comparator;

/**
 * 作者：cjy
 * 类名：ProductDateComparator
 * 全路径类名：com.example.tmall.comparator.ProductDateComparator
 * 父类或接口：@see Comparator
 * 描述：产品日期比较器
 */
public class ProductDateComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return p1.getCreateDate().compareTo(p2.getCreateDate());
    }

}
