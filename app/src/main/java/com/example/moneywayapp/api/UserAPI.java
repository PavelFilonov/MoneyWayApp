package com.example.moneywayapp.api;

import com.example.moneywayapp.model.Empty;
import com.example.moneywayapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {

    @POST("login")
    Call<Empty> login(@Body User user);
}
