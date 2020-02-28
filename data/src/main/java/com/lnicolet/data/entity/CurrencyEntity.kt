package com.lnicolet.data.entity

import com.google.gson.annotations.JsonAdapter
import com.lnicolet.data.deserializer.CurrencyEntityDeserializer
import com.lnicolet.domain.model.CurrenciesExchangeModel
import com.lnicolet.domain.model.CurrencyModel

@JsonAdapter(CurrencyEntityDeserializer::class)
data class CurrencyEntity(
    val baseCurrency: String,
    val rates: List<RateEntity>
) {
    fun toDomain(): CurrenciesExchangeModel? = CurrenciesExchangeModel(
        CurrencyModel.valueOf(baseCurrency),
        rates.map { it.toDomain() }
    )
}