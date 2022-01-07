package com.currency.lesson1.api

import com.currency.lesson1.models.Currencies
import com.currency.lesson1.models.Rate
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiInterface {

    @GET("api/v7/convert")
    suspend fun convertRate(
        @Query("q") q:String,
        @Query("compact") compact: String = "ultra",
        @Query("apiKey") apiKey:String
    ): Rate

    @GET("api/v7/currencies")
    suspend fun currencies(
        @Query("apiKey") apiKey:String
    ): Currencies?

}