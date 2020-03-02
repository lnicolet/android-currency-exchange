package com.lnicolet.data.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.lnicolet.data.entity.CurrencyEntity
import com.lnicolet.data.entity.RateEntity
import java.lang.reflect.Type

class CurrencyEntityDeserializer : JsonDeserializer<CurrencyEntity> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CurrencyEntity {
        val jsonObject = json.asJsonObject
        val baseCurrencyString = if (jsonObject.has(BASE_CURRENCY_KEY)) {
            jsonObject.get(BASE_CURRENCY_KEY).asString
        } else {
            ""
        }
        val rates = mutableListOf<RateEntity>()
        val ratesJsonObject = jsonObject.getAsJsonObject(RATES_KEY)
        ratesJsonObject.keySet().forEach { key ->
            rates.add(RateEntity(key, ratesJsonObject.get(key).asDouble))
        }

        return CurrencyEntity(baseCurrency = baseCurrencyString, rates = rates)
    }

}

private const val BASE_CURRENCY_KEY = "baseCurrency"
private const val RATES_KEY = "rates"