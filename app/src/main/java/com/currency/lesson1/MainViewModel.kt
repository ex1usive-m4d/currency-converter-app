package com.currency.lesson1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.models.CurrencyRateResponse
import kotlinx.coroutines.launch

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    val apiResponse: MutableLiveData<CurrencyRateResponse> = MutableLiveData()

    fun getCurrencyRate(from: String, to: String) {
        viewModelScope.launch {
            val rate: CurrencyRateResponse = apiRepository.getCurrencyResponse(from.toString(), to.toString())
            Log.d("resp", rate.toString())
            apiResponse.value = rate
        }
    }
}