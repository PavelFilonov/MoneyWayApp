package com.example.moneywayapp.api;

import com.example.moneywayapp.model.Category;
import com.example.moneywayapp.model.Empty;

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
    Call<List<Category>> getAll();

    @DELETE("categories/{id}")
    Call<Empty> delete(@Path(value = "id") Long id);

    @PUT("categories/{id}")
    Call<Empty> rename(@Path(value = "id") Long id, @Query(value = "name") String name);

    @POST("categories")
    Call<Empty> add(@Body Category category);
}
