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
import com.currency.lesson1.models.CURRENCY_EUR
import com.currency.lesson1.models.CURRENCY_USD
import com.currency.lesson1.util.Utility
import com.currency.lesson1.util.Utility.convertResult
import okhttp3.internal.toImmutableList

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { MainViewModelFactory(ApiRepository(this.applicationContext), this) }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val currencyAdapter by lazy {
        ArrayAdapter<String>(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        arrayListOf(CURRENCY_USD, CURRENCY_EUR)
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        currencyAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFrom?.adapter = currencyAdapter
        binding.spinnerTo?.adapter = currencyAdapter

        viewModel.rateData.observe(this, Observer {
            binding.resultValue?.text = viewModel.getFormattedResult()
        })

        viewModel.status.observe(this, { status ->
            setStatus(status)
        })

        viewModel.currenciesList.observe(this, Observer { response ->
            updateCurrencyAdapterItems(response)
        })

        binding.convertBtn?.setOnClickListener {
            viewModel.calculateCurrencyRate(binding.spinnerFrom?.selectedItem.toString(), binding.spinnerTo?.selectedItem.toString())
        }

        binding.tryAgainButton?.setOnClickListener {
            viewModel.pingStatus()
            viewModel.calculateCurrencyRate(binding.spinnerFrom?.selectedItem.toString(), binding.spinnerTo?.selectedItem.toString())
        }
    }

    fun setStatus(status: NetworkApiStatus?) = with(binding) {
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

    fun updateCurrencyAdapterItems(items: List<String>) {
        currencyAdapter?.clear()
        currencyAdapter?.addAll(items)
        currencyAdapter?.notifyDataSetChanged()
    }

}