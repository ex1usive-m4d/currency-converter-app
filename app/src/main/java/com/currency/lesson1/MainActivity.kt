package com.currency.lesson1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.api.CurrencyApiInterface
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private var spinnerFrom: Spinner? = null
    private var spinnerTo: Spinner? = null
    private var currencyInput: EditText? = null
    private var convertBtn: Button? = null
    private var resultInfo: TextView? = null

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        currencyInput = findViewById(R.id.editInput)
        resultInfo = findViewById(R.id.resultValue)
        convertBtn = findViewById(R.id.convertBtn)

        convertBtn?.setOnClickListener {
            var apiResponse: String? = null;
            if (this.isBlankInput()) {
                Toast.makeText(this, "Введите значение", Toast.LENGTH_LONG).show()
            } else {
                currencyRateCalculation(spinnerFrom?.selectedItem.toString(), spinnerTo?.selectedItem.toString())
            }
        }
    }

    private fun currencyRateCalculation(from: String, to: String) {
        val currencyRepository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(currencyRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getCurrencyRate(from.toString(), to.toString())
        viewModel.apiResponse.observe(this, Observer {response ->
            Log.d("rs", response.toString())
            resultInfo?.text = "Результат:".plus(convertResult(response, currencyInput?.text.toString().toDouble()))
        })
    }

    private fun convertResult(currencyRate: Double, inputValue: Double): Double? {
        return inputValue * currencyRate;
    }

    private fun isBlankInput(): Boolean {
        return this.currencyInput?.text?.toString()?.trim().isNullOrBlank()
    }
}