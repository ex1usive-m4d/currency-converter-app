package com.currency.lesson1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.currency.lesson1.api.ApiRepository

class MainViewModelFactory(private val apiRepository: ApiRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(apiRepository) as T
    }


}