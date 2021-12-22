package com.currency.lesson1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.lesson1.api.ApiRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    val apiResponse: MutableLiveData<Double> = MutableLiveData()
    val currenciesList: MutableLiveData<MutableList<String>> = MutableLiveData()

    fun getCurrenciesList() {
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
    }

    fun getCurrencyRate(from: String, to: String) {
        viewModelScope.launch {
            val response: Response<ResponseBody> = apiRepository.getCurrencyResponse(from.toString(), to.toString())
            if (response.isSuccessful) {
                val data: String? = response.body()?.string()
                val rateValue: Double = JSONObject(data).get(from.plus("_").plus(to)).toString().toDouble()
                apiResponse.value = rateValue
            }
        }
    }
}