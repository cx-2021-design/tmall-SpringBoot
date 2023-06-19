package com.example.tmall.pojo;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.example.tmall.service.OrderService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

@Entity
@Table(name = "order_")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = { "handler","hibernateLazyInitializer"})
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    private String orderCode;
    private String address;
    private String post;
    private String receiver;
    private String mobile;
    private String userMessage;
    private Date createDate;
    private Date payDate;
    private Date deliveryDate;
    private Date confirmDate;
    private String status;

    @Transient
    private List<OrderItem> orderItems;
    @Transient
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money total;
    @Transient
    private int totalNumber;
    @Transient
    private String statusDesc;
    public String getStatusDesc(){
        if(null!=statusDesc)
            return statusDesc;
        String desc ="未知";
        switch(status){
            case OrderService.waitPay:
                desc="待付";
                break;
            case OrderService.waitDelivery:
                desc="待发";
                break;
            case OrderService.waitConfirm:
                desc="待收";
                break;
            case OrderService.waitReview:
                desc="等评";
                break;
            case OrderService.finish:
                desc="完成";
                break;
            case OrderService.delete:
                desc="刪除";
                break;
            default:
                desc="未知";
        }
        statusDesc = desc;
        return statusDesc;
    }
}
