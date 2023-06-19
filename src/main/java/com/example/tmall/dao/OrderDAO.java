package com.example.tmall.dao;

import com.example.tmall.pojo.Order;
import com.example.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order,Integer> {
    Order findById(int id);
    //用来获取那些某个用户的订单，但是状态又不是 "delete" 的订单。
    public List<Order> findByUserAndStatusNotOrderByIdDesc(User user, String status);
}