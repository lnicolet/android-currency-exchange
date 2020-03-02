package com.lnicolet.data.entity

import com.lnicolet.domain.model.CurrencyModel
import com.lnicolet.domain.model.RateModel

data class RateEntity(
    val currency: String,
    val value: Double
) {
    fun toDomain() =
        RateModel(CurrencyModel.valueOf(currency), value)
}