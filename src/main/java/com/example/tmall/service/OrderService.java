package com.example.tmall.service;

import com.example.tmall.dao.OrderDAO;
import com.example.tmall.pojo.Order;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.pojo.User;
import com.example.tmall.util.Page4Navigator;
import com.example.tmall.util.SpringContextUtil;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@CacheConfig(cacheNames="orders")
public class OrderService {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    @Autowired OrderDAO orderDAO;
    @Autowired OrderItemService orderItemService;

    @Cacheable(key="'orders-page-'+#p0+ '-' + #p1")
    public Page4Navigator<Order> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =orderDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
   //将订单的订单项设置为空。订单中有订单项属性，订单项中有订单属性，在转化为Json时会无穷递归的转化进去死循环
    public void removeOrderFromOrderItem(List<Order> orders) {
        for (Order order : orders) {
            removeOrderFromOrderItem(order);
        }
    }

    public void removeOrderFromOrderItem(Order order) {
        List<OrderItem> orderItems= order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(null);
        }
    }
    @Cacheable(key="'orders-one-'+ #p0")
    public Order get(int oid) {
        return orderDAO.findById(oid);
    }

    @CacheEvict(allEntries=true)
    public void update(Order bean) {
        orderDAO.save(bean);
    }

    @CacheEvict(allEntries=true)
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public Money add(Order order, List<OrderItem> ois) {
        Money total = Money.of(CurrencyUnit.of("CNY"),0);
        add(order);

//        if(false)
//            throw new RuntimeException();

        for (OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemService.update(oi);
            BigDecimal orderItemNumber= BigDecimal.valueOf(oi.getNumber());
            total=total.plus(oi.getProduct().getPromotePrice().getAmount().multiply(orderItemNumber));
        }
        return total;
    }

    @CacheEvict(allEntries=true)
    public void add(Order order) {
        orderDAO.save(order);
    }

    public List<Order> listByUserWithoutDelete(User user) {
        OrderService orderService = SpringContextUtil.getBean(OrderService.class);
        List<Order> orders = orderService.listByUserAndNotDeleted(user);
        orderItemService.fill(orders);
        return orders;
    }
    @Cacheable(key="'orders-uid-'+ #p0.id")
    public List<Order> listByUserAndNotDeleted(User user) {
        return orderDAO.findByUserAndStatusNotOrderByIdDesc(user, OrderService.delete);
    }
   // 计算订单总金额
    public void cacl(Order o) {
        List<OrderItem> orderItems = o.getOrderItems();
        Money total = Money.of(CurrencyUnit.of("CNY"),0);
        for (OrderItem orderItem : orderItems) {
            BigDecimal orderItemNumber= BigDecimal.valueOf(orderItem.getNumber());
            total=total.plus(orderItem.getProduct().getPromotePrice().getAmount().multiply(orderItemNumber));
        }
        o.setTotal(total);
    }

}