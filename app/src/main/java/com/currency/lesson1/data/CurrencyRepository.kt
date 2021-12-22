package com.currency.lesson1.data

import androidx.lifecycle.LiveData

class CurrencyRepository(private val CurrencyDao: CurrencyDao) {

    val readAllData: LiveData<List<Currency>> = CurrencyDao.readAllData()

    suspend fun addCurrency(Currency: Currency) {
        CurrencyDao.addCurrency(Currency)
    }

    suspend fun getCurrency(currency: String): LiveData<Currency> {
       return CurrencyDao.findByRate(currency)
    }
}