package com.lnicolet.currencyexchange.exchangelist.model

import com.lnicolet.domain.model.CurrencyModel

data class CurrencyExchange(
    val currencyModel: CurrencyModel,
    val exchangeRate: Double,
    var exchangeConversion: Double
)