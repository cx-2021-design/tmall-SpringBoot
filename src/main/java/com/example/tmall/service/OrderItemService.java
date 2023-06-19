package com.example.tmall.service;

import com.example.tmall.dao.OrderItemDAO;
import com.example.tmall.pojo.Order;
import com.example.tmall.pojo.OrderItem;
import com.example.tmall.pojo.Product;
import com.example.tmall.pojo.User;
import com.example.tmall.util.SpringContextUtil;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.List;

/**
 * 作者：cjy
 * 类名：OrderItemService
 * 全路径类名：com.example.tmall.service.OrderItemService
 * 父类或接口：
 * 描述：订单项服务
 */
@Service
@CacheConfig(cacheNames="orderItems")
public class OrderItemService {
    @Autowired
    OrderItemDAO orderItemDAO;
    @Autowired ProductImageService productImageService;

    public void fill(List<Order> orders) {
        for (Order order : orders)
            fill(order);
    }

    /**
     * 方法名：fill
     * 传入参数：@param order 订单
     * 异常类型：
     * 返回类型：
     * 描述：从数据库中取出来的 Order 是没有 OrderItem集合的，
     *      这里通过 OrderItemDAO 取出来再放在 Order的 orderItems属性上。
     */
    public void fill(Order order) {
        OrderItemService orderItemService = SpringContextUtil.getBean(OrderItemService.class);
        List<OrderItem> orderItems = orderItemService.listByOrder(order);
        Money total = Money.of(CurrencyUnit.of("CNY"),0);
        int totalNumber = 0;
        for (OrderItem oi :orderItems) {
            BigDecimal orderItemNumber= BigDecimal.valueOf(oi.getNumber());
            total=total.plus(oi.getProduct().getPromotePrice().getAmount().multiply(orderItemNumber));
            totalNumber+=oi.getNumber();
            productImageService.setFirstProductImage(oi.getProduct());
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);
        order.setTotalNumber(totalNumber);
    }
    @Cacheable(key="'orderItems-oid-'+ #p0.id")
    public List<OrderItem> listByOrder(Order order) {
        return orderItemDAO.findByOrderOrderByIdDesc(order);
    }

    @CacheEvict(allEntries=true)
    public void update(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }

    @CacheEvict
    public void add(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }

    @Cacheable(key="'orderItems-one-'+ #p0")
    public OrderItem get(int id) {
        return orderItemDAO.findById(id);
    }

    @CacheEvict(allEntries=true)
    public void delete(int id) {
        orderItemDAO.delete(id);
    }
    public int getSaleCount(Product product) {
        OrderItemService orderItemService = SpringContextUtil.getBean(OrderItemService.class);
        List<OrderItem> ois =orderItemService.listByProduct(product);
        int result =0;
        for (OrderItem oi : ois) {
            if(null!=oi.getOrder())
                if(null!= oi.getOrder() && null!=oi.getOrder().getPayDate())
                    result+=oi.getNumber();
        }
        return result;
    }
    @Cacheable(key="'orderItems-pid-'+ #p0.id")
    public List<OrderItem> listByProduct(Product product) {
        return orderItemDAO.findByProduct(product);
    }
    @Cacheable(key="'orderItems-uid-'+ #p0.id")
    public List<OrderItem> listByUser(User user) {
        return orderItemDAO.findByUserAndOrderIsNull(user);
    }
}
