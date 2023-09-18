package com.example.ecommerceapi.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String username;
    @Column(nullable = false,length = 1000)
    private String password;
    @Column(nullable = false,unique = true, length = 320)
    private String email;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.EAGER)
    @OrderBy("id desc ")
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    @Column(nullable = false)
    private Boolean emailVerified = false;
}
