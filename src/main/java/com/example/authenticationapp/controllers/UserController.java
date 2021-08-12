package com.example.authenticationapp.controllers;

import com.example.authenticationapp.entities.User;
import com.example.authenticationapp.services.SecurityService;
import com.example.authenticationapp.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/signup-user")
    public User registerUser(@RequestBody User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userDetailsService.registerUser(user);
    }

    @PostMapping("/login-user")
    public String loginUser(@RequestBody User user){
        boolean result = securityService.login(user.getEmail(), user.getPassword());
        if(result!=true) {
            return "Username Not Found, Please Check Your Credentials And Try Again";
        }else

        return "Login Succesful";
    }

}
