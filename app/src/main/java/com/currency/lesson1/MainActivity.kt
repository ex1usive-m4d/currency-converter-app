package com.currency.lesson1

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import android.widget.ArrayAdapter





class MainActivity : AppCompatActivity() {

    private var spinnerFrom: Spinner? = null
    private var spinnerTo: Spinner? = null
    private var currencyInput: EditText? = null
    private var convertBtn: Button? = null
    private var resultInfo: TextView? = null
    private var currencyAdapter: ArrayAdapter<String>? = null
    private var currencyRepository = ApiRepository()
    private var viewModelFactory = MainViewModelFactory(currencyRepository)

    lateinit var viewModel: MainViewModel

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        currencyInput = findViewById(R.id.editInput)
        resultInfo = findViewById(R.id.resultValue)
        convertBtn = findViewById(R.id.convertBtn)

        if (isInternetAvailable(this)) {
            this.initCollectData()
        }

        convertBtn?.setOnClickListener {
            if (this.isBlankInput()) {
                Toast.makeText(this, "Введите значение", Toast.LENGTH_LONG).show()
            } else {
                this.viewModel.getCurrencyRate(spinnerFrom?.selectedItem.toString(), spinnerTo?.selectedItem.toString())
            }
        }
    }

    private fun initCollectData()
    {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getCurrencyRate(spinnerFrom?.selectedItem.toString(), spinnerTo?.selectedItem.toString())
        viewModel.apiResponse.observe(this, Observer {response ->
            Log.d("rs", response.toString())
            resultInfo?.text = "Результат:".plus(convertResult(response, currencyInput?.text.toString().toDouble()))
        })

        viewModel.getCurrenciesList()
        viewModel.currenciesList.observe(this, Observer { response ->
            Log.d("respObs", response.toMutableList().toString())
            currencyAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                response.toTypedArray()
            )
            currencyAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item,)
            spinnerFrom?.setAdapter(currencyAdapter)
            spinnerTo?.setAdapter(currencyAdapter)
        })
    }

    fun isInternetAvailable(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    private fun convertResult(currencyRate: Double, inputValue: Double): Double? {
        return inputValue * currencyRate;
    }

    private fun isBlankInput(): Boolean {
        return this.currencyInput?.text?.toString()?.trim().isNullOrBlank()
    }
}