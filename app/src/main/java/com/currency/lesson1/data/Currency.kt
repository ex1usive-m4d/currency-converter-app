package com.currency.lesson1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_table")
data class Currency (
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val rate: String,
    val to: String,
    val fr: String
)
