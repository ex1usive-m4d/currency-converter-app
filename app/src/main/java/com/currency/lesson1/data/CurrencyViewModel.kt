package com.currency.lesson1.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllData: LiveData<List<Currency>>
    private val repository: CurrencyRepository

    init {
        val currencyDao = CurrencyDatabase.getDatabase(application).CurrencyDao()
        repository = CurrencyRepository(currencyDao)
        readAllData = repository.readAllData
    }

    fun addCurrency(Currency: Currency) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCurrency(Currency)
        }
    }

    fun getCurrencyRate(from: String, to: String) {
        viewModelScope.launch {
            val CurrencyFrom : LiveData<Currency> = repository.getCurrency(from)
            val CurrencyTo : LiveData<Currency> = repository.getCurrency(to)

        }
    }
}