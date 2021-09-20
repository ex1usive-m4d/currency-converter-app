package com.currency.lesson1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.models.CurrencyRateResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    val apiResponse: MutableLiveData<Double> = MutableLiveData()

    fun getCurrencyRate(from: String, to: String) {
        viewModelScope.launch {
            val rate: Response<ResponseBody> = apiRepository.getCurrencyResponse(from.toString(), to.toString())
            if (rate.isSuccessful) {
                val data: String? = rate.body()?.string()
                val rateValue: Double = JSONObject(data).get(from.plus("_").plus(to)).toString().toDouble()
                Log.d("resp", rate.toString())
                apiResponse.value = rateValue
            }
        }
    }
}