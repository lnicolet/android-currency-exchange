package com.lnicolet.domain.model

data class CurrenciesExchangeModel(val baseCurrency: CurrencyModel, val rates: List<RateModel>)