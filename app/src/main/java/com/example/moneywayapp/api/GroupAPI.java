package com.example.moneywayapp.api;

import com.example.moneywayapp.model.dto.Group;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GroupAPI {

    @GET("groups/{id}")
    Call<Group> getById(@Path(value = "id") Long id);

    @GET("groups")
    Call<Group> getByToken(@Query(value = "token") String token);

    @POST("groups")
    Call<Void> add(@Body Group group);

    @DELETE("groups/{id}")
    Call<Void> deleteById(@Path(value = "id") Long id);

    @DELETE("groups/{id}/users")
    Call<Void> deleteUser(@Path(value = "id") Long id, @Query("userLogin") String userLogin);

    @POST("groups/{id}/users")
    Call<Void> addUser(@Path(value = "id") Long id);

    @GET("groups/users")
    Call<List<Group>> getByUser();

    @GET("groups/{id}/users")
    Call<String> getUsers(@Path(value = "id") Long id);

    @PUT("groups/{id}")
    Call<Void> rename(@Path(value = "id") Long id, @Query("name") String name);
}
