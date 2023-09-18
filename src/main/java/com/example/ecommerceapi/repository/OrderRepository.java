package com.example.ecommerceapi.repository;

import com.example.ecommerceapi.entity.Orders;
import com.example.ecommerceapi.entity.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends ListCrudRepository<Orders,Long> {
    List<Orders> findByUser(User user);
}
