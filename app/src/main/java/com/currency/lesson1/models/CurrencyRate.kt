package com.currency.lesson1.models

import com.google.gson.annotations.SerializedName

data class CurrencyRateResponse(val response: String)


data class Rate(val rate: String)

class Currencies (
    val currencies: List<Currency>?
)

class Currency(
    val key: String
)

data class CurrencyRate(
    val id: String,
    @SerializedName("val")
    val rate: String,
    val to: String,
    val fr: String
    )

