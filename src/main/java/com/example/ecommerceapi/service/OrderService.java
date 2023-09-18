package com.example.ecommerceapi.service;

import com.example.ecommerceapi.entity.Orders;
import com.example.ecommerceapi.entity.User;
import com.example.ecommerceapi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class OrderService {
    @Autowired
    private  OrderRepository orderRepository;

    public List<Orders> getUserOrders(User user){
        return orderRepository.findByUser(user);
    }
}
