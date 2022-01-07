package com.currency.lesson1.util

import android.content.Context
import com.currency.lesson1.BuildConfig
import com.currency.lesson1.api.CurrencyApiInterface
import com.currency.lesson1.models.Rate
import com.currency.lesson1.models.RateDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.currency.lesson1.api.NetworkConnectionInterceptor
import com.currency.lesson1.models.*
import okhttp3.*
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private var API_URL = BuildConfig.API_URL

    val gsonCurrencies by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<Currencies?>() {}.type,
                CurrenciesDeserializer()
            )
            .create()
    }


    val gsonRate by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<Rate?>() {}.type,
                RateDeserializer()
            )
            .create()
    }

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(API_URL)
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
    }

    fun provideWebService(context: Context, gson: Gson): CurrencyApiInterface {
        val okHttpClient = httpClient.addInterceptor(NetworkConnectionInterceptor(context)).build()
        return Retrofit.Builder().baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CurrencyApiInterface::class.java)
    }

}