package com.example.moneywayapp.api;

import com.example.moneywayapp.model.context.DateOperationContext;
import com.example.moneywayapp.model.dto.Operation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OperationAPI {

    @POST("operations")
    Call<Void> add(@Body Operation operation);

    @POST("operations/filter")
    Call<List<Operation>> getByCategoryAndPeriod(@Body DateOperationContext dateOperationContext);
}
