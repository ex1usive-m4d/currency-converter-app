package com.currency.lesson1.util

import com.currency.lesson1.api.CurrencyApiInterface
import com.currency.lesson1.data.new.Currencies
import com.currency.lesson1.data.new.CurrenciesDeserializer
import com.currency.lesson1.data.new.CurrencyEntityV2
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<Currencies?>() {}.type,
                CurrenciesDeserializer()
            )
            .create()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://free.currconv.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiCurrencyService: CurrencyApiInterface by lazy {
        retrofit.create(CurrencyApiInterface::class.java)
    }

}