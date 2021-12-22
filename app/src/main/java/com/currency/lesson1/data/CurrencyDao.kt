package com.currency.lesson1.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCurrency(currency: Currency)

    @Query("SELECT * FROM currency_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Currency>>

    @Query("SELECT * FROM currency_table WHERE rate = (:rate) ORDER BY id ASC LIMIT 1")
    fun findByRate(rate: String): LiveData<Currency>

}