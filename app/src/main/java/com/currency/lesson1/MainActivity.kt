package com.currency.lesson1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.api.NetworkApiStatus
import com.currency.lesson1.api.NoConnectivityException
import com.currency.lesson1.databinding.ActivityMainBinding
import com.currency.lesson1.util.Utility
import com.currency.lesson1.util.Utility.convertResult
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import java.io.IOException
import java.lang.RuntimeException

//view
class MainActivity : AppCompatActivity() {

    private var spinnerFrom: SearchableSpinner? = null
    private var spinnerTo: SearchableSpinner? = null
    private var convertBtn: Button? = null
    private var resultInfo: TextView? = null
    private var progressBar: ProgressBar? = null
    private var currencyAdapter: ArrayAdapter<String>? = null
    private var currencyRepository = ApiRepository(this)

    lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            networkCollectData()
            bindElements()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
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
            viewModel.calculateCurrencyRate(spinnerFrom?.selectedItem.toString(), spinnerTo?.selectedItem.toString())
        }
    }

    private fun networkCollectData()
    {
        viewModel = MainViewModel(currencyRepository)
        viewModel.rateData.observe(this, Observer {rate ->
                resultInfo?.text = "Результат:".plus(convertResult(rate.rate.toDouble(), "1".toDouble()))
        })
        viewModel.status.observe(this, { status ->
            bindStatus(status)
        })

        viewModel.currenciesList.observe(this, Observer { response ->
            currencyAdapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                response.toTypedArray()
            )
            currencyAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom?.adapter = currencyAdapter
            spinnerTo?.adapter = currencyAdapter
        })
    }

    fun bindStatus(status: NetworkApiStatus?) {
        when (status) {
            NetworkApiStatus.LOADING -> {
                resultInfo?.visibility = View.INVISIBLE
                progressBar?.visibility = View.VISIBLE
            }

            NetworkApiStatus.ERROR -> {
                resultInfo?.visibility = View.INVISIBLE
                progressBar?.visibility = View.INVISIBLE
                Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show()
            }

            NetworkApiStatus.DONE -> {
                resultInfo?.visibility = View.VISIBLE
                progressBar?.visibility = View.INVISIBLE
            }
        }
    }

}