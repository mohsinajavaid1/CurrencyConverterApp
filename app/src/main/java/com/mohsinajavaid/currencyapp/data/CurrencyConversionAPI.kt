package com.mohsinajavaid.currencyapp.data

import com.mohsinajavaid.currencyapp.model.ConvertedCurrencyRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyConversionAPI {

    @GET("v2.0/rates/latest")
    suspend fun getExchangeRates(@QueryMap queries: Map<String, String>): Response<ConvertedCurrencyRate>
}
