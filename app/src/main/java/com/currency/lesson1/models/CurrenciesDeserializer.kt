package com.currency.lesson1.models

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type




class CurrenciesDeserializer : JsonDeserializer<Currencies?> {

    override fun deserialize(
        elem: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?
    ): Currencies? {
        val deserializer = jsonDeserializationContext ?: return null

        return elem
            .takeIf { it.isJsonObject }
            ?.asJsonObject
            ?.entrySet()
            ?.mapNotNull { if (it.value.isJsonObject) it.value.asJsonObject else null }
            ?.let { list: List<JsonObject> ->
                mutableListOf<Currency>().apply {
                    list.forEach { jsonObject ->
                        jsonObject.entrySet()
                            .map { Currency(it.key) }
                            .let { list -> addAll(list) }
                    }
                }
            }.let { Currencies(it) }
    }
}

class RateDeserializer : JsonDeserializer<Rate?> {

    override fun deserialize(
        elem: JsonElement,
        type: Type?,
        jsonDeserializationContext: JsonDeserializationContext?
    ): Rate {
        return elem.asJsonObject
            .entrySet()
            .last()
            .let {
               Rate(mapOf(it.key to it.value.toString().toDouble()))
            }
    }
}