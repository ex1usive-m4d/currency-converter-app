package com.currency.lesson1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.models.CurrencyRate
import com.currency.lesson1.util.Utility
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    val apiResponse: MutableLiveData<Double> = MutableLiveData()
    val currenciesList: MutableLiveData<MutableList<String>> = MutableLiveData()
    var cacheLastRate: CurrencyRate? = null

    fun getCurrenciesList() {
        if (currenciesList.value.isNullOrEmpty()) {
            viewModelScope.launch {
                val response: Response<ResponseBody> = apiRepository.getCurrenciesList()
                if (response.isSuccessful) {
                    val data: String? = response.body()?.string()
                    val currencies: MutableList<String> = ArrayList<String>()
                    val keys = (JSONObject(data).get("results") as JSONObject).keys()
                    for (key in keys) {
                        currencies.add(key)
                    }
                    currenciesList.value = currencies
                    Log.d("resp", response.toString())
                }
            }
        } else {
            Log.d("cache", currenciesList.value.toString())
        }
    }

    fun getCurrencyRate(from: String, to: String) {
        if (cacheLastRate?.id.equals(Utility.getCurrencyString(from, to))) {
            apiResponse.value = cacheLastRate?.rate?.toDouble()
        } else {
            viewModelScope.launch {
                val response: Response<ResponseBody> =
                    apiRepository.getCurrencyResponse(from.toString(), to.toString())
                if (response.isSuccessful) {
                    val data: String? = response.body()?.string()
                    val rateValue: Double =
                        JSONObject(data).get(Utility.getCurrencyString(from, to)).toString()
                            .toDouble()
                    apiResponse.value = rateValue
                    cacheLastRate = CurrencyRate(
                        Utility.getCurrencyString(from, to),
                        rateValue.toString(),
                        from,
                        to
                    )
                }
            }
        }
    }
}