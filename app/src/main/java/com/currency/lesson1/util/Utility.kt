package com.currency.lesson1.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.widget.EditText

object Utility {

    fun convertResult(currencyRate: Double, inputValue: Double): Double? {
        return inputValue * currencyRate;
    }

     fun isBlankInput(currencyInput: EditText): Boolean {
        return currencyInput.text?.toString()?.trim().isNullOrBlank()
     }

    fun getCurrencyString(from: String, to: String): String {
        return from.plus("_").plus(to)
    }
}