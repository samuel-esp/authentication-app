package com.example.authenticationapp.services;

public interface SecurityService {

    boolean login(String username, String password);

    String getCurrentLogin();

}
