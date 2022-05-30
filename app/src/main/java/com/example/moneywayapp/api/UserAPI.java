package com.example.moneywayapp.api;

import com.example.moneywayapp.model.Empty;
import com.example.moneywayapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserAPI {

    @POST("login")
    Call<Empty> login(@Body User user);

    @POST("register")
    Call<Empty> register(@Body User user);

    @GET("users/profile")
    Call<User> profile();

    @PUT("users/profile/email")
    Call<Empty> updateEmail(@Query(value = "email") String email);

    @PUT("users/profile/login")
    Call<Empty> updateLogin(@Query(value = "login") String login);

    @PUT("users/profile/password")
    Call<Empty> updatePassword(@Query(value = "password") String password);
}
