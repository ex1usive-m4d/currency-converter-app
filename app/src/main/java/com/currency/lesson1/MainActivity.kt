package com.currency.lesson1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.currency.lesson1.api.ApiRepository
import android.widget.ArrayAdapter
import com.currency.lesson1.databinding.ActivityMainBinding
import com.currency.lesson1.util.Utility
import com.currency.lesson1.util.Utility.convertResult

//view
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
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindElements()
        if (Utility.isNetworkAvailable(this)) {
            networkCollectData()
        }
        convertBtn?.setOnClickListener {
            viewModel.getCurrencyRate(spinnerFrom?.selectedItem.toString(), spinnerTo?.selectedItem.toString())
        }
    }

    private fun bindElements()
    {
        spinnerFrom = binding.spinnerFrom
        spinnerTo = binding.spinnerTo
        currencyInput = binding.editInput
        resultInfo = binding.resultValue
        convertBtn = binding.convertBtn
    }

    private fun networkCollectData()
    {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.apiResponse.observe(this, Observer {response ->
            Log.d("rs", response.toString())
            if (currencyInput?.let { input -> Utility.isBlankInput(input) } == true) {
                Toast.makeText(this, "Введите значение", Toast.LENGTH_LONG).show()
                resultInfo?.text = "Введите значение!"
            } else {
                resultInfo?.text = "Результат:".plus(convertResult(response, currencyInput?.text.toString().toDouble()))
            }
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

}