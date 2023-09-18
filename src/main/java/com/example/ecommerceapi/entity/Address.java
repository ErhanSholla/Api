package com.example.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address_line_1",nullable = false,length = 512)
    private String addressLine1;
    @Column(name = "address_line_2")
    private String addressLine2;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false,length = 75)
    private String country;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
