package com.example.moneywayapp.api;

import com.example.moneywayapp.model.dto.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CategoryOfGroupAPI {

    @GET("categories/groups/{groupId}")
    Call<List<Category>> get(@Path(value = "groupId") Long groupId);

    @DELETE("categories/{id}/groups/{groupId}")
    Call<Void> delete(@Path(value = "id") Long id, @Path(value = "groupId") Long groupId);

    @PUT("categories/{id}/groups/{groupId}")
    Call<Void> rename(@Path(value = "id") Long id, @Path(value = "groupId") Long groupId, @Query(value = "name") String name);

    @POST("categories/groups/{groupId}")
    Call<Void> add(@Body Category category, @Path(value = "groupId") Long groupId);
}
