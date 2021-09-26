package com.currency.lesson1.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class CurrencyRateResponse(val response: String)

data class CurrencyModel(val currency: String)

data class CurrencyRate(
    val id: String,
    @SerializedName("val")
    val rate: String,
    val to: String,
    val fr: String
)

