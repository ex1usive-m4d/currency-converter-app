package com.currency.lesson1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.currency.lesson1.api.ApiRepository
import android.widget.ArrayAdapter
import com.currency.lesson1.databinding.ActivityMainBinding
import com.currency.lesson1.util.Utility
import com.currency.lesson1.util.Utility.convertResult
import com.toptoche.searchablespinnerlibrary.SearchableSpinner

//view
class MainActivity : AppCompatActivity() {

    private var spinnerFrom: SearchableSpinner? = null
    private var spinnerTo: SearchableSpinner? = null
    private var convertBtn: Button? = null
    private var resultInfo: TextView? = null
    private var progressBar: ProgressBar? = null
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

        if (Utility.isNetworkAvailable(this)) {
            networkCollectData()
            bindElements()
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_LONG).show()
            resultInfo?.text = "Нет подключения к сети Интернет!"
        }
    }

    private fun bindElements()
    {
        spinnerFrom = binding.spinnerFrom
        spinnerTo = binding.spinnerTo
        resultInfo = binding.resultValue
        convertBtn = binding.convertBtn
        progressBar = binding.progressBar

        convertBtn?.setOnClickListener {
            resultInfo?.visibility = View.GONE
            progressBar?.visibility = ProgressBar.VISIBLE
            convertBtn?.visibility = View.INVISIBLE
            viewModel.getCurrencyRate(spinnerFrom?.selectedItem.toString(), spinnerTo?.selectedItem.toString())
        }
    }

    private fun networkCollectData()
    {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.apiResponse.observe(this, Observer {response ->
            Log.d("rs", response.toString())
                resultInfo?.text = "Результат:".plus(convertResult(response, "1".toDouble()))
                resultInfo?.visibility = View.VISIBLE
                progressBar?.visibility = ProgressBar.INVISIBLE
                convertBtn?.visibility = View.VISIBLE
                convertBtn?.text = "Конвертировать"
        })
        viewModel.getCurrenciesList()
        viewModel.currenciesList.observe(this, Observer { response ->
            currencyAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                response.toTypedArray()
            )
            currencyAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom?.setAdapter(currencyAdapter)
            spinnerTo?.setAdapter(currencyAdapter)
        })
    }

}