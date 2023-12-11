package com.mohsinajavaid.currencyapp.di

import android.content.Context
import com.mohsinajavaid.currencyapp.BuildConfig
import com.mohsinajavaid.currencyapp.data.CurrencyConversionAPI
import com.mohsinajavaid.currencyapp.data.CurrencyDataSource
import com.mohsinajavaid.currencyapp.data.DataStoreManager
import com.mohsinajavaid.currencyapp.data.Repository
import com.mohsinajavaid.currencyapp.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideCurrencyRatesApi(retrofit: Retrofit): CurrencyConversionAPI {
        return retrofit.create(CurrencyConversionAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context) = DataStoreManager(context)

    @Provides
    @Singleton
    fun provideNetworkUtils(@ApplicationContext context: Context) = NetworkUtils(context)

    @Provides
    @Singleton
    fun provideCurrencyDataStore(currencyRatesApi:CurrencyConversionAPI,networkUtils:NetworkUtils) = CurrencyDataSource(currencyRatesApi , networkUtils)
    @Provides
    @Singleton
    fun provideRepository(currencyDataSource: CurrencyDataSource) = Repository(currencyDataSource)


}