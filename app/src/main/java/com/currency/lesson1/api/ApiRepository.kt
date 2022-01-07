package com.currency.lesson1.api

import android.content.Context
import com.currency.lesson1.BuildConfig
import com.currency.lesson1.models.Currency
import com.currency.lesson1.util.RetrofitInstance
import com.currency.lesson1.util.Utility

enum class NetworkApiStatus { LOADING, ERROR, DONE, NO_CONNECT, INIT }

class ApiRepository(context: Context) {

    private val API_KEY: String = BuildConfig.API_KEY
    private val context: Context = context

    suspend fun convertRate(
        currencyFrom: String,
        currencyTo: String
    ): Map<String, Double>? {
        return RetrofitInstance.provideWebService(context, RetrofitInstance.gsonRate)
            .convertRate(Utility.getCurrencyString(currencyFrom, currencyTo), "ultra", API_KEY).rate ?: emptyMap()
    }

    suspend fun ping(): Boolean {
        return !convertRate("USD", "USD").isNullOrEmpty()
    }

    suspend fun getCurrenciesList(): List<String> =
        RetrofitInstance.provideWebService(context, RetrofitInstance.gsonCurrencies).currencies(this.API_KEY)?.currencies?.asSequence()
            ?.map { it.key }
            ?.sorted()
            ?.toList() ?: listOf("USD", "EUR", "RUB")
}