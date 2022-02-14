package org.server.remoteclass.dto;

import lombok.*;
import org.server.remoteclass.constant.OrderStatus;
import org.server.remoteclass.constant.Payment;
import org.server.remoteclass.entity.Coupon;
import org.server.remoteclass.entity.Order;
import org.server.remoteclass.entity.OrderLecture;
import org.server.remoteclass.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class OrderDto {
    private Long orderId;
    private Long userId;
    private List<OrderLecture> orderLectures = new ArrayList<>();
    private OrderStatus orderStatus; //주문상태
    private LocalDateTime orderDate;
    private Long couponId;       //적용하는 쿠폰 아이디
    private Payment payment; //결제방법
    private String bank;  //입금은행
    private String account;  //예금주
}
