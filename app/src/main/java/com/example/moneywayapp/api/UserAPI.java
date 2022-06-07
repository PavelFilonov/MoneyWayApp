package com.example.moneywayapp.api;

import com.example.moneywayapp.model.dto.UserDTO;
import com.example.moneywayapp.model.response.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPI {

    @POST("login")
    Call<AuthResponse> login(@Body UserDTO user);

    @POST("register")
    Call<AuthResponse> register(@Body UserDTO user);

    @GET("users/profile")
    Call<UserDTO> profile();

    @GET("users/{id}")
    Call<UserDTO> getLoginById(@Path(value = "id") Long id);

    @PUT("users/profile/email")
    Call<Void> updateEmail(@Query(value = "email") String email);

    @PUT("users/profile/login")
    Call<Void> updateLogin(@Query(value = "login") String login);

    @PUT("users/profile/password")
    Call<Void> updatePassword(@Query(value = "password") String password);
}
