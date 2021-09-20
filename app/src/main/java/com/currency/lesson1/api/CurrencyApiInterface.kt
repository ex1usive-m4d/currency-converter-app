package com.currency.lesson1.api

import com.currency.lesson1.models.CurrencyRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiInterface {

    @GET("api/v7/convert")
    suspend fun getCurrencyRateResponse(
        @Query("q") q:String,
        @Query("compact") compact: String = "ultra",
        @Query("apiKey") apiKey:String
    ): CurrencyRateResponse

    suspend fun getCurrencyRate(
        @Query("q") q:String,
        @Query("apiKey") apiKey:String
    ): CurrencyRateResponse

}