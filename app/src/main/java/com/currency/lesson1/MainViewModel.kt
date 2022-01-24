package com.currency.lesson1

import android.util.Log
import androidx.lifecycle.*
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.api.NetworkApiStatus
import com.currency.lesson1.api.NoConnectivityException
import com.currency.lesson1.models.CURRENCIES_STATE_KEY
import com.currency.lesson1.models.CurrencyRate
import com.currency.lesson1.models.ModelState
import com.currency.lesson1.util.Utility
import kotlinx.coroutines.launch

class MainViewModel(
    private val apiService: ApiRepository,
    private val handle: SavedStateHandle
) : ViewModel() {

    val rateData: MutableLiveData<CurrencyRate> = MutableLiveData()
    val currenciesList: MutableLiveData<List<String>> = MutableLiveData()
    private val _status = MutableLiveData<NetworkApiStatus>()

    val status: LiveData<NetworkApiStatus>
        get() = _status

    init {
        _status.value = NetworkApiStatus.INIT
        if (!handle.contains(CURRENCIES_STATE_KEY)) {
            collectCurrenciesList()
        } else {
            val modelState = handle.getLiveData<ModelState>(CURRENCIES_STATE_KEY)
            currenciesList.value = modelState.value?.currencies
        }
    }

    fun pingStatus()
    {
        viewModelScope.launch {
            try {
                _status.value = NetworkApiStatus.LOADING
                if (apiService.ping()) {
                    _status.value = NetworkApiStatus.INIT
                } else {
                    _status.value = NetworkApiStatus.NO_CONNECT
                }
            } catch (e: NoConnectivityException) {
                _status.value = NetworkApiStatus.NO_CONNECT
            }
        }
    }

    fun collectCurrenciesList() {
        if (currenciesList.value == null) {
            viewModelScope.launch {
                try {
                    val response: List<String> = apiService.getCurrenciesList()
                    Log.d("resp", response.toString())
                    currenciesList.value = response
                    handle.set(CURRENCIES_STATE_KEY, ModelState(response))
                    _status.value = NetworkApiStatus.DONE
                } catch (e: NoConnectivityException) {
                    _status.value = NetworkApiStatus.NO_CONNECT
                }
            }
        }
    }

    fun calculateCurrencyRate(from: String, to: String) {
        viewModelScope.launch {
            try {
                _status.value = NetworkApiStatus.LOADING
                val response: Map<String, Double>? = apiService.convertRate(from, to)
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
            } catch (e: NoConnectivityException) {
                _status.value = NetworkApiStatus.NO_CONNECT
            } catch (e: Exception) {
                _status.value = NetworkApiStatus.ERROR
            }
        }
    }

    fun getFormattedResult(): String {
        return "Результат:".plus(rateData.value?.rate?.toDouble()
            ?.let { Utility.convertResult(it, "1".toDouble()) })
    }
}