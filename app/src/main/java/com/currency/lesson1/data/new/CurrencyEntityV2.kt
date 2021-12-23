package com.currency.lesson1.data.new

import com.google.gson.annotations.SerializedName

class Currencies(
    val currencies: List<CurrencyEntityV2>?
)

class CurrencyEntityV2(
    val key: String,
    val currency: CurrencyV2
)

class CurrencyV2 {
    @SerializedName("currencyName")
    val currencyName: String? = null

    @SerializedName("currencySymbol")
    val currencySymbol: String? = null

    @SerializedName("id")
    val id: String? = null
}