package com.example.ecommerceapi.service;

import com.example.ecommerceapi.dto.UserDTO;
import com.example.ecommerceapi.exception.UserAlreadyExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private void executeInsertStatements() {
        jdbcTemplate.execute("INSERT INTO users (email, first_name, last_name, password, username, email_verified) " +
                "VALUES ('UserA@junit.com', 'UserA-FirstName', 'UserA-LastName', '$2a$10$hBn5gu6cGelJNiE6DDsaBOmZgyumCSzVwrOK/37FWgJ6aLIdZSSI2', 'UserA', true)");

        jdbcTemplate.execute("INSERT INTO users (email, first_name, last_name, password, username, email_verified) " +
                "VALUES ('UserB@junit.com', 'UserB-FirstName', 'UserB-LastName', '$2a$10$TlYbg57fqOy/1LJjispkjuSIvFJXbh3fy0J9fvHnCpuntZOITAjVG', 'UserB', false)");
    }
    @BeforeEach
    public void setUp() {
        executeInsertStatements();
    }
    @Autowired
    private UserService userService;
    @Test
    @Transactional
    public void testRegisterUser(){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("UserA");
        userDTO.setEmail("UserA@junit.com");
        userDTO.setFirstname("UserA-FirstName");
        userDTO.setLastname("UserA-LastName");
        userDTO.setPassword("MySecretePassword123");
        Assertions.assertThrows(UserAlreadyExistException.class,
                ()->userService.registerUser(userDTO),"User should already in");
        userDTO.setUsername("UserServiceTest%tesRegisterUser");
        userDTO.setEmail("UserA@junit.com");
        Assertions.assertThrows(UserAlreadyExistException.class,
                ()-> userService.registerUser(userDTO),"Email already in use");
    }
}
