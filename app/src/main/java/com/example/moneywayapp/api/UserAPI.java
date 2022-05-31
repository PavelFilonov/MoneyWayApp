package com.example.moneywayapp.api;

import com.example.moneywayapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserAPI {

    @POST("login")
    Call<Void> login(@Body User user);

    @POST("register")
    Call<Void> register(@Body User user);

    @POST("logout")
    Call<Void> logout();

    @GET("users/profile")
    Call<User> profile();

    @PUT("users/profile/email")
    Call<Void> updateEmail(@Query(value = "email") String email);

    @PUT("users/profile/login")
    Call<Void> updateLogin(@Query(value = "login") String login);

    @PUT("users/profile/password")
    Call<Void> updatePassword(@Query(value = "password") String password);
}
