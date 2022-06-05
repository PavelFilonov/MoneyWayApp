package com.example.moneywayapp.model.response;

import com.example.moneywayapp.model.dto.UserDTO;

public class AuthResponse {
    private UserDTO user;
    private String token;

    public AuthResponse(UserDTO user, String token) {
        this.user = user;
        this.token = token;
    }

    public AuthResponse() {
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
