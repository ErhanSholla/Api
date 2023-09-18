package com.example.ecommerceapi.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserNotVerifiedException extends Exception{
    private final boolean newEmailSent;

    public boolean isNewEmailSent() {
        return newEmailSent;
    }
}
