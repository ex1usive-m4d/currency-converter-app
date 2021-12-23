package com.currency.lesson1.data.new

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
                mutableListOf<CurrencyEntityV2>().apply {
                    list.forEach { jsonObject ->
                        jsonObject.entrySet()
                            .map {
                                CurrencyEntityV2(
                                    it.key,
                                    deserializer.deserialize(it.value, CurrencyV2::class.java)
                                )
                            }.let { list -> addAll(list) }
                    }
                }
            }
            .let { Currencies(it) }
    }
}