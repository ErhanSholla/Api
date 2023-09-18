package com.example.ecommerceapi.service;

import com.example.ecommerceapi.dto.LoginDTO;
import com.example.ecommerceapi.dto.PasswordResetBody;
import com.example.ecommerceapi.dto.UserDTO;
import com.example.ecommerceapi.entity.User;
import com.example.ecommerceapi.entity.VerificationToken;
import com.example.ecommerceapi.exception.EmailFailureException;
import com.example.ecommerceapi.exception.EmailNotFoundException;
import com.example.ecommerceapi.exception.UserAlreadyExistException;
import com.example.ecommerceapi.exception.UserNotVerifiedException;
import com.example.ecommerceapi.repository.UserRepository;
import com.example.ecommerceapi.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSentService emailSentService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private  JWTService jwtService;
    public User registerUser(UserDTO userDTO) throws UserAlreadyExistException, EmailFailureException {
        if(userRepository.findByEmailIgnoreCase(userDTO.getEmail()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(userDTO.getUsername()).isPresent()){
            throw new UserAlreadyExistException();
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        VerificationToken verificationToken = createVerificationToken(user);
        emailSentService.setVerificationEmail(verificationToken);
        return userRepository.save(user);
    }

    private VerificationToken createVerificationToken(User user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerificationJWT(user));
        verificationToken.setCreatedTimeStamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    public String loginUser(LoginDTO loginDTO) throws UserNotVerifiedException, EmailFailureException {
        Optional<User> user = userRepository.findByUsernameIgnoreCase(loginDTO.getUsername());
        if(user.isPresent()){
            User user1 = user.get();
            if(passwordEncoder.matches(loginDTO.getPassword(),user1.getPassword())){
                if(user.get().getEmailVerified()){
                    return jwtService.generateJwt(user1);
                }else {
                    List<VerificationToken> verificationTokens = user.get().getVerificationTokens();
                    boolean resend = verificationTokens.size() == 0 ||
                            verificationTokens.get(0).getCreatedTimeStamp().
                                    before(new Timestamp(System.currentTimeMillis()- (60*60*1000)));

                    if(resend){
                        VerificationToken verificationToken = createVerificationToken(user1);
                        verificationTokenRepository.save(verificationToken);
                        emailSentService.setVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }

            }
        }
        return null;
    }

    @Transactional
    public boolean verifyUser(String token){
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken.isPresent()){
            VerificationToken verificationToken1 = verificationToken.get();
            User user = verificationToken1.getUser();
            if(!user.getEmailVerified()){
                user.setEmailVerified(true);
                userRepository.save(user);
                verificationTokenRepository.deleteByUser(user);
                return true;
            }
        }
        return false;
    }

    public void forgotPassword(String email) throws EmailNotFoundException, EmailFailureException {
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if(user.isPresent()){
            User user1 = user.get();
            String token = jwtService.generateResetPasswordJWT(user1);
            emailSentService.sentPasswordResetEmail(user1,token);
        }else {
            throw new EmailNotFoundException();
        }
    }

    public void resetPassword(PasswordResetBody passwordResetBody){
        String email = jwtService.getResetPasswordEmail(passwordResetBody.getToken());
        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if(user.isPresent()){
            User user1 = user.get();
            user1.setPassword(passwordEncoder.encode(passwordResetBody.getPassword()));
            userRepository.save(user1);
        }

    }
}
