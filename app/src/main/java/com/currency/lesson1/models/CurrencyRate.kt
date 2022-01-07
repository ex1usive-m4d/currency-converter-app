package com.currency.lesson1.models

import android.os.Parcelable
import com.currency.lesson1.MainViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Rate(val rate: Map<String, Double>)

class Currencies (
    var currencies: List<Currency>?
)
@Parcelize
class ModelState(
    val currencies: List<String>
): Parcelable

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

