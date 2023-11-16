package com.mohsinajavaid.currencyapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsinajavaid.currencyapp.BuildConfig
import com.mohsinajavaid.currencyapp.data.Repository
import com.mohsinajavaid.currencyapp.model.ConvertedCurrencyRate
import com.mohsinajavaid.currencyapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(val repository: Repository) : ViewModel() {

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
        /*  }
      else {
            //  exchangeRatesResponse.value = NetworkResult.Error(message = "No Internet Connection")
          }*/
    }

    private fun handleExchangeRatesResponse(data:NetworkResult<ConvertedCurrencyRate>): NetworkResult<ConvertedCurrencyRate> {
        return data
    }

    fun getToValue(value: String, rates: Map<String, String>): Double {
      return rates[value]!!.toDouble()

    }

    /* private fun checkInternetConnection(): Boolean {
         val connectivityManager =
             getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         val activeNetwork = connectivityManager.activeNetwork ?: return false
         val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

         return when {
             capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
             capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
             capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
             else -> false
         }


 }*/

}