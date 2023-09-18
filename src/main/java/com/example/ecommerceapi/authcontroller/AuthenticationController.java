package com.example.ecommerceapi.authcontroller;

import com.example.ecommerceapi.dto.LoginDTO;
import com.example.ecommerceapi.dto.LoginJWT;
import com.example.ecommerceapi.dto.PasswordResetBody;
import com.example.ecommerceapi.dto.UserDTO;
import com.example.ecommerceapi.entity.User;
import com.example.ecommerceapi.exception.EmailFailureException;
import com.example.ecommerceapi.exception.EmailNotFoundException;
import com.example.ecommerceapi.exception.UserAlreadyExistException;
import com.example.ecommerceapi.exception.UserNotVerifiedException;
import com.example.ecommerceapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody UserDTO userDTO)  {
        try{
            userService.registerUser(userDTO);
            return ResponseEntity.ok().build();
        }catch (UserAlreadyExistException exception){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (EmailFailureException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginJWT> loginUser(@Valid @RequestBody LoginDTO loginDTO) throws UserNotVerifiedException, EmailFailureException {
        String jwt = null;
        try {
            jwt = userService.loginUser(loginDTO);
        }catch (UserNotVerifiedException ex){
            LoginJWT loginJWT = new LoginJWT();
            loginJWT.setSuccess(false);
            String reason = "User Not Verified";
            if(ex.isNewEmailSent()){
                reason += "_EMAIL_RESENT";
            }
            loginJWT.setFailureReason(reason);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginJWT);
        }catch (EmailFailureException exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        if (jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            LoginJWT response = new LoginJWT();
            response.setJwt(jwt);
            response.setSuccess(true);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/me")
    public User getLoggedUserProfile(@AuthenticationPrincipal User user){
        return user;
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token) {
        if (userService.verifyUser(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity forgotPassword(@RequestParam String email){
        try {
            userService.forgotPassword(email);
            return ResponseEntity.ok().build();
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (EmailFailureException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetBody passwordResetBody){
        userService.resetPassword(passwordResetBody);
        return ResponseEntity.ok().build();
    }

}
