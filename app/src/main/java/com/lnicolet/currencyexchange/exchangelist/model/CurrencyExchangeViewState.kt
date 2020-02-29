package com.lnicolet.currencyexchange.exchangelist.model

import com.lnicolet.currencyexchange.core.Status

data class CurrencyExchangeViewState(
    val status: Status = Status.Loading
)