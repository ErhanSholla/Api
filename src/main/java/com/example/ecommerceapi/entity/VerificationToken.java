package com.example.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(nullable = false,unique = true)
    private String token;
    @Column(nullable = false)
    private Timestamp createdTimeStamp;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
