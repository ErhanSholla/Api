package com.example.ecommerceapi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginJWT {
    private String jwt;
    private boolean success;
    private String failureReason;

}
