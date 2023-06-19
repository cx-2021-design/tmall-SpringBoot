package com.example.tmall.dao;

import com.example.tmall.pojo.Order;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByOrderOrderByIdDesc(Order order);
    List<OrderItem> findByProduct(Product product);
    OrderItem findById(int id);
    void delete(int id);
    //一个订单项的订单为null，那么它意味着该订单项还没有被添加到任何订单中
    List<OrderItem> findByUserAndOrderIsNull(User user);
}