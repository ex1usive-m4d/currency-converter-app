package com.currency.lesson1.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.widget.EditText

object Utility {

    //check if network is connected
    fun isNetworkAvailable(context: Context?): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    fun convertResult(currencyRate: Double, inputValue: Double): Double? {
        return inputValue * currencyRate;
    }

     fun isBlankInput(currencyInput: EditText): Boolean {
        return currencyInput.text?.toString()?.trim().isNullOrBlank()
    }
}