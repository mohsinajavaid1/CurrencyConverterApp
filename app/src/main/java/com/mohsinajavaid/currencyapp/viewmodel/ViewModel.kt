package com.mohsinajavaid.currencyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsinajavaid.currencyapp.data.DataStoreManager
import com.mohsinajavaid.currencyapp.data.Repository
import com.mohsinajavaid.currencyapp.model.ConvertedCurrencyRate
import com.mohsinajavaid.currencyapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStore: DataStoreManager,
) : ViewModel() {

    var exchangeRatesResponse: MutableLiveData<NetworkResult<ConvertedCurrencyRate>> =
        MutableLiveData()

    fun getExchangeRates() {
        viewModelScope.launch {
            getExchangeRatesSafeCall()
        }
    }


    private suspend fun getExchangeRatesSafeCall() {
        // if (checkInternetConnection()) {
        try {
            val response = repository.remote.getExchangeRates()
            exchangeRatesResponse.value = handleExchangeRatesResponse(response)
        } catch (e: Exception) {
            exchangeRatesResponse.value =
                NetworkResult.Error(message = "No Response. Try Again!!")
        }

    }

    private fun handleExchangeRatesResponse(data: NetworkResult<ConvertedCurrencyRate>): NetworkResult<ConvertedCurrencyRate> {
        return data
    }

    fun getToValue(value: String, rates: Map<String, String>): Double {
        return rates[value]!!.toDouble()

    }


}