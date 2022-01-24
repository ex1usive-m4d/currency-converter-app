package com.currency.lesson1.util

import android.widget.EditText

object Utility {

    fun convertResult(currencyRate: Double, inputValue: Double): Double? {
        return inputValue * currencyRate;
    }

    fun getCurrencyString(from: String, to: String): String {
        return from.plus("_").plus(to)
    }
}