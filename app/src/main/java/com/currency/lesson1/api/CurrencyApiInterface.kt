package com.currency.lesson1.api

import com.currency.lesson1.models.CurrencyRateResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiInterface {

    @GET("api/v7/convert")
    suspend fun getCurrencyRateResponse(
        @Query("q") q:String,
        @Query("compact") compact: String = "ultra",
        @Query("apiKey") apiKey:String
    ): CurrencyRateResponse

    @GET("api/v7/convert")
    suspend fun rawRateResponse(
        @Query("q") q:String,
        @Query("compact") compact: String = "ultra",
        @Query("apiKey") apiKey:String
    ): Response<ResponseBody>

}