package com.mohsinajavaid.currencyapp.data

import com.mohsinajavaid.currencyapp.BuildConfig
import com.mohsinajavaid.currencyapp.model.ConvertedCurrencyRate
import com.mohsinajavaid.currencyapp.utils.NetworkResult
import com.mohsinajavaid.currencyapp.utils.NetworkUtils
import retrofit2.Response
import javax.inject.Inject

class CurrencyDataSource @Inject constructor(
    private val currencyRatesApi: CurrencyConversionAPI,
    private val networkUtils: NetworkUtils
) {

    suspend fun getExchangeRates(): NetworkResult<ConvertedCurrencyRate> {


        try {
            return if (networkUtils.isConnectedToInternet) {


                val queries = HashMap<String, String>()
                queries["apikey"] = BuildConfig.API_KEY
                queries["base"] = "USD"

                val apiResponse: Response<ConvertedCurrencyRate> =
                    currencyRatesApi.getExchangeRates(queries)
                val data = apiResponse.body()
                if (apiResponse.isSuccessful && data != null) {

                    NetworkResult.Success(data)

                } else {

                    NetworkResult.Error(
                        null, apiResponse.message()
                    )

                }

            } else {
                NetworkResult.Error(
                    null,
                    "Connect your internet"
                )

            }
        } catch (e: Throwable) {
            return NetworkResult.Error(
                null,
                "Something went wrong"
            )
        }
    }
}
