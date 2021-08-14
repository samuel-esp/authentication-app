package com.example.authenticationapp.controllers;

import com.example.authenticationapp.entities.PasswordResetToken;
import com.example.authenticationapp.entities.User;
import com.example.authenticationapp.services.SecurityService;
import com.example.authenticationapp.services.UserDetailsServiceImpl;
import com.example.authenticationapp.utilities.EmailSingleton;
import com.example.authenticationapp.utilities.EmailSubmit;
import com.example.authenticationapp.utilities.PasswordJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Properties;

@RestController
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JavaMailSender emailSender;

    Logger logger = LoggerFactory.getLogger(UserController.class);

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

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody EmailSubmit emailSubmit) {
        User user = userDetailsService.getUserByEmail(emailSubmit);
        if (user == null) {
            return "Email not recognized";
        }
        String token = java.util.UUID.randomUUID().toString();
        userDetailsService.createPasswordResetTokenForUser(user, token);
        emailSender.send(userDetailsService.constructResetTokenEmail(token, user));
        return "Email Was Sent To Provided Email Address";
    }

    @GetMapping("/user/changePasswordToken/{token}")
    public boolean validTokenForPasswordReset(@PathVariable String token) {
        PasswordResetToken passwordResetToken = userDetailsService.getTokenFromString(token);
        logger.info(passwordResetToken.getUser().getEmail());
        if(passwordResetToken == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        boolean result = userDetailsService.validatePasswordResetToken(passwordResetToken.getToken());
        return result;
    }

    @PostMapping("/user/changePasswordWToken/{token}")
    public String changePasswordWithToken(@PathVariable String token, @RequestBody PasswordJson password) {
        User user = userDetailsService.getTokenFromString(token).getUser();
        return userDetailsService.changeUserPassword(user, encoder.encode(password.getPassword()));
    }

    @GetMapping("/user/currentUser")
    public User getCurrentUser() {
        String userEmail = securityService.getCurrentLogin();
        logger.info(userEmail);
        if(userEmail.equals("anonymousUser")){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User user = userDetailsService.getUserByEmail(userEmail);
        return user;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(EmailSingleton.getInstance().getEmailAddress());
        mailSender.setPassword(EmailSingleton.getInstance().getEmailAddressPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
