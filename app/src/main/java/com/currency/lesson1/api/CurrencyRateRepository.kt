package com.currency.lesson1.api

import androidx.lifecycle.MutableLiveData

class CurrencyRateRepository {

    val currenciesList: MutableLiveData<MutableList<String>> = MutableLiveData()

}