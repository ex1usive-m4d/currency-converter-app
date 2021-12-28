package com.currency.lesson1

import android.R
import android.content.Context
import android.util.Log
import android.widget.*
import androidx.lifecycle.*
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.api.NetworkApiStatus
import com.currency.lesson1.models.CurrencyRate
import com.currency.lesson1.util.Utility
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    val rateData: MutableLiveData<CurrencyRate> = MutableLiveData()
    val result: MutableLiveData<String> = MutableLiveData("Результат:")
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
                    async {
                        val response: List<String> = apiRepository.getCurrenciesList()
                            .asSequence()
                            .map { it.key }
                            .sorted()
                            .toList()
                        Log.d("resp", response.toString())
                        currenciesList.value = response
                        _status.value = NetworkApiStatus.DONE
                    }.await()
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
                    async {
                        val response: Response<ResponseBody> = apiRepository.convertRate(from, to)
                        val data: String? = response.body()?.string()
                        val rateValue: Double =
                            JSONObject(data).get(Utility.getCurrencyString(from, to)).toString()
                                .toDouble()
                        rateData.value = CurrencyRate(
                            Utility.getCurrencyString(from, to),
                            rateValue.toString(),
                            from,
                            to
                        )
                        _status.value = NetworkApiStatus.DONE
                    }.await()
                } catch (e: RuntimeException) {
                    _status.value = NetworkApiStatus.ERROR
                }
            }
        }
    }
}