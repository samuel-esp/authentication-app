package com.example.authenticationapp.services;

import antlr.Token;
import com.example.authenticationapp.controllers.UserController;
import com.example.authenticationapp.entities.PasswordResetToken;
import com.example.authenticationapp.entities.User;
import com.example.authenticationapp.repositories.PasswordResetTokenRepository;
import com.example.authenticationapp.repositories.UserRepository;
import com.example.authenticationapp.utilities.EmailSubmit;
import com.example.authenticationapp.utilities.EmailSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    Logger logger = LoggerFactory.getLogger(UserController.class);


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoleList());

    }

    public User registerUser(User user){
        return userRepository.save(user);
    }

    public String changeUserPassword(User user, String password) {
        user.setPassword(password);
        userRepository.save(user);
        return "Password was succesfully updated";
    }

    public User getUserByEmail(EmailSubmit emailSubmit){
        return userRepository.findByEmail(emailSubmit.getEmail());
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    public SimpleMailMessage constructResetTokenEmail(String token, User user) {
        String url = "/user/changePasswordToken/" + token;
        return constructEmail("Reset Password", "Follow this link to reset your password\n" + " \r\n" + url, user);
    }

    public PasswordResetToken getTokenFromString(String token){
        PasswordResetToken tokenPass = passwordResetTokenRepository.findByToken(token);
        return tokenPass;
    }

    public SimpleMailMessage constructEmail(String subject, String body,
                                             User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(EmailSingleton.getInstance().getEmailAddress());
        return email;
    }

    public boolean validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        logger.info(passToken.getToken());
        logger.info(passToken.getUser().getEmail());
        isTokenFound(passToken);

        /*isTokenExpired(passToken);*/

        return true;

    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    /*
    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
*/


}
