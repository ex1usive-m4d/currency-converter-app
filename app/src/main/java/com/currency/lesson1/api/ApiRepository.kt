package com.currency.lesson1.api

import com.currency.lesson1.models.CurrencyRateResponse
import com.currency.lesson1.util.RetrofitInstance

class ApiRepository {

    private val API_KEY : String = "27aa03909cf691819561";

    suspend fun getCurrencyRate(currencyFrom: String, currencyTo: String): CurrencyRateResponse {
        return RetrofitInstance.apiCurrencyService
            .getCurrencyRate(currencyFrom.plus("_").plus(currencyTo), this.API_KEY)
    }

    suspend fun getCurrencyResponse(currencyFrom: String, currencyTo: String): CurrencyRateResponse {
        return RetrofitInstance.apiCurrencyService
            .getCurrencyRateResponse(currencyFrom.plus("_").plus(currencyTo),"ultra", this.API_KEY)
    }
}