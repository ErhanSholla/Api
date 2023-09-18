package com.example.ecommerceapi.controller;

import com.example.ecommerceapi.entity.Orders;
import com.example.ecommerceapi.entity.User;
import com.example.ecommerceapi.service.OrderService;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping()
    public List<Orders> getOrders(@AuthenticationPrincipal User user){
        return orderService.getUserOrders(user);
    }
}
