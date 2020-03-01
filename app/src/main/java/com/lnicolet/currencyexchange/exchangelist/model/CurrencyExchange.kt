package com.lnicolet.currencyexchange.exchangelist.model

import com.lnicolet.domain.model.CurrencyModel

data class CurrencyExchange(
    val currencyModel: CurrencyModel,
    var exchangeRate: Double,
    var baseValue: Double
) {
    fun getExchangeConversion() = baseValue * exchangeRate
}