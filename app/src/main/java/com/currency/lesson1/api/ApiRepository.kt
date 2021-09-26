package com.currency.lesson1.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.currency.lesson1.models.CurrencyRateResponse
import com.currency.lesson1.util.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Response

class ApiRepository {

    private val API_KEY : String = "27aa03909cf691819561";

    suspend fun getCurrencyResponse(currencyFrom: String, currencyTo: String): Response<ResponseBody> {
        return RetrofitInstance.apiCurrencyService
            .rawRateResponse(currencyFrom.plus("_").plus(currencyTo),"ultra", this.API_KEY)
    }

    suspend fun getCurrenciesList(): Response<ResponseBody> {
        return RetrofitInstance.apiCurrencyService
            .getListCurrencies(this.API_KEY)
    }
}