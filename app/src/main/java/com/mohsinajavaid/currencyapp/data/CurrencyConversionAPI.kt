package com.mohsinajavaid.currencyapp.data

import com.mohsinajavaid.currencyapp.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyConversionAPI {

    @GET("v1/latest")
    suspend fun getExchangeRates(@QueryMap queries: Map<String, String>): Response<ApiResponse>
}
