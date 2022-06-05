package com.example.moneywayapp.api;

import com.example.moneywayapp.model.dto.OperationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OperationAPI {

    @POST("operations")
    Call<Void> add(@Body OperationDTO operation);

    @POST("operations/filter")
    Call<List<OperationDTO>> getByCategoryAndPeriod(
            @Query("categoryId") Long categoryId,
            @Query("fromDate") String fromDate,
            @Query("toDate") String toDate);
}
