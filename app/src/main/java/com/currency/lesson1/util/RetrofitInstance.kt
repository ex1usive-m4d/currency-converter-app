package com.currency.lesson1.util

import android.content.Context
import com.currency.lesson1.BuildConfig
import com.currency.lesson1.api.CurrencyApiInterface
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.currency.lesson1.api.NetworkConnectionInterceptor
import com.currency.lesson1.models.*
import okhttp3.*
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private var API_URL = BuildConfig.API_URL

    private val gsonCurrencies by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<Currencies?>() {}.type,
                CurrenciesDeserializer()
            )
            .create()
    }


    private val gsonRate by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<CurrencyRateResponse?>() {}.type,
                RateDeserializer()
            )
            .create()
    }


    fun provideWebService(context: Context): CurrencyApiInterface {
        val okHttpClient = OkHttpClient.Builder()

        okHttpClient
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(context))

        val builder = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gsonCurrencies))

        return builder.build().create(CurrencyApiInterface::class.java)
    }

}