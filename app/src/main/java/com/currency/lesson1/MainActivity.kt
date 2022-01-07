package com.currency.lesson1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.currency.lesson1.api.ApiRepository
import com.currency.lesson1.api.NetworkApiStatus
import com.currency.lesson1.databinding.ActivityMainBinding
import com.currency.lesson1.util.Utility.convertResult

class MainActivity : AppCompatActivity() {


    private var currencyAdapter: ArrayAdapter<String>? = null
    private var currencyRepository = ApiRepository(this)

    private val viewModel by viewModels<MainViewModel> { MainViewModelFactory(currencyRepository, this) }
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.rateData.observe(this, Observer {rate ->
            binding.resultValue?.text = "Результат:".plus(convertResult(rate.rate.toDouble(), "1".toDouble()))
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
            binding.spinnerFrom?.adapter = currencyAdapter
            binding.spinnerTo?.adapter = currencyAdapter
        })

        binding.convertBtn?.setOnClickListener {
            viewModel.calculateCurrencyRate(binding.spinnerFrom?.selectedItem.toString(), binding.spinnerTo?.selectedItem.toString())
        }

        binding.tryAgainButton?.setOnClickListener {
            viewModel.pingStatus()
            viewModel.calculateCurrencyRate(binding.spinnerFrom?.selectedItem.toString(), binding.spinnerTo?.selectedItem.toString())
        }
    }

    fun bindStatus(status: NetworkApiStatus?) = with(binding) {
        when (status) {
            NetworkApiStatus.LOADING -> {
                resultValue?.visibility = View.INVISIBLE
                progressBar?.visibility = View.VISIBLE
            }

            NetworkApiStatus.ERROR -> {
                resultValue?.visibility = View.INVISIBLE
                progressBar?.visibility = View.INVISIBLE
            }

            NetworkApiStatus.DONE -> {
                resultValue?.visibility = View.VISIBLE
                progressBar?.visibility = View.INVISIBLE
            }

            NetworkApiStatus.NO_CONNECT -> {
                noInternetLayout?.visibility = View.VISIBLE
                internetLayout?.visibility = View.INVISIBLE
            } else -> {
                noInternetLayout?.visibility = View.INVISIBLE
                internetLayout?.visibility = View.VISIBLE
            }

        }
    }

}