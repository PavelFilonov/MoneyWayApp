package com.example.moneywayapp.api;

import com.example.moneywayapp.model.Operation;
import com.example.moneywayapp.model.OperationRequestContext;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OperationAPI {

    @POST("operations")
    Call<Void> add(@Body Operation operation);

    @GET("operations")
    Call<List<Operation>> getByCategoryAndPeriod(@Body OperationRequestContext operationRequestContext);
}
