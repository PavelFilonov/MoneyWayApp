package com.example.moneywayapp.api;

import com.example.moneywayapp.model.dto.Category;
import com.example.moneywayapp.model.dto.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryOfUserAPI {

    @GET("categories")
    Call<List<Category>> get(@Body User user);

    @DELETE("categories/{id}")
    Call<Void> delete(@Path(value = "id") Long id);

    @PUT("categories/{id}")
    Call<Void> rename(@Path(value = "id") Long id, @Query(value = "name") String name);

    @POST("categories")
    Call<Void> add(@Body UserCategoryContext userCategoryContext);
}
