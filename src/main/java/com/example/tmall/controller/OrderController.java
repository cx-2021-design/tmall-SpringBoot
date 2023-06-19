package com.example.tmall.controller;

import com.example.tmall.pojo.Order;
import com.example.tmall.service.OrderItemService;
import com.example.tmall.service.OrderService;
import com.example.tmall.util.Page4Navigator;
import com.example.tmall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    /**
     * 获取订单列表，并填充订单的订单项信息
     *
     * @param start 起始位置
     * @param size  每页数量
     * @return 分页后的订单列表
     * @throws Exception 异常
     */
    @GetMapping("/orders")
    public Page4Navigator<Order> list(@RequestParam(value = "start", defaultValue = "0") int start,
                                      @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page4Navigator<Order> page = orderService.list(start, size, 5);
        orderItemService.fill(page.getContent());
        orderService.removeOrderFromOrderItem(page.getContent());
        return page;
    }

    /**
     * 将订单标记为已发货
     *
     * @param oid 订单ID
     * @return 操作结果
     * @throws IOException IO异常
     */
    @PutMapping("deliveryOrder/{oid}")
    public Object deliveryOrder(@PathVariable int oid) throws IOException {
        Order o = orderService.get(oid);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        orderService.update(o);
        return Result.success();
    }
}
