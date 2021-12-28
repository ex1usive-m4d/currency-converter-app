package com.currency.lesson1.api

import android.content.Context
import com.currency.lesson1.BuildConfig
import com.currency.lesson1.models.Currency
import com.currency.lesson1.models.CurrencyRate
import com.currency.lesson1.models.CurrencyRateResponse
import com.currency.lesson1.models.Rate
import com.currency.lesson1.util.RetrofitInstance
import com.currency.lesson1.util.Utility
import okhttp3.ResponseBody
import retrofit2.Response

enum class NetworkApiStatus { LOADING, ERROR, DONE }

class ApiRepository(context: Context) {

    private val API_KEY: String = BuildConfig.API_KEY
    private val context: Context = context

    suspend fun convertRate(
        currencyFrom: String,
        currencyTo: String
    ): Map<String, Double>? {
        return RetrofitInstance.provideConvertService(context)
            .convertRate(Utility.getCurrencyString(currencyFrom, currencyTo), "ultra", API_KEY).rate ?: emptyMap()
    }

    suspend fun getCurrenciesList(): List<Currency> =
        RetrofitInstance.provideWebService(context).currencies(this.API_KEY)?.currencies ?: emptyList()
}