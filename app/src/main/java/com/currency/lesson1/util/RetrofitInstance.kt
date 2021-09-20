package com.currency.lesson1.util

import com.currency.lesson1.api.CurrencyApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://free.currconv.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiCurrencyService: CurrencyApiInterface by lazy {
        retrofit.create(CurrencyApiInterface::class.java)
    }

}