package com.currency.lesson1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.api.NetworkApiStatus
import com.currency.lesson1.models.CurrencyRate
import com.currency.lesson1.util.Utility
import kotlinx.coroutines.launch

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    val rateData: MutableLiveData<CurrencyRate> = MutableLiveData()
    val currenciesList: MutableLiveData<List<String>> = MutableLiveData()
    private val _status = MutableLiveData<NetworkApiStatus>()

    val status: LiveData<NetworkApiStatus>
        get() = _status

    init {
        collectCurrenciesList()
    }

    private fun collectCurrenciesList() {
        if (currenciesList.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val response: List<String> = apiRepository.getCurrenciesList()
                        .asSequence()
                        .map { it.key }
                        .sorted()
                        .toList()
                    Log.d("resp", response.toString())
                    currenciesList.value = response
                    _status.value = NetworkApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = NetworkApiStatus.ERROR
                }
            }
        }
    }

    fun calculateCurrencyRate(from: String, to: String) {
        if (!rateData.value?.id.equals(Utility.getCurrencyString(from, to))) {
            viewModelScope.launch {
                try {
                    _status.value = NetworkApiStatus.LOADING
                    val response: Map<String, Double>? = apiRepository.convertRate(from, to)
                    if (!response.isNullOrEmpty()) {
                        rateData.value = CurrencyRate(
                            response.keys.first(),
                            response.getValue(response.keys.first()).toString(),
                            from,
                            to
                        )
                        _status.value = NetworkApiStatus.DONE
                    } else {
                        throw RuntimeException("Ошибка получения данных")
                    }
                } catch (e: RuntimeException) {
                    _status.value = NetworkApiStatus.ERROR
                }
            }
        }
    }
}