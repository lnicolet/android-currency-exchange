package com.lnicolet.domain.repository

import com.lnicolet.domain.model.CurrenciesExchangeModel
import io.reactivex.Single

interface CurrencyRepository {
    fun getCurrencyByBase(base: String): Single<CurrenciesExchangeModel>
}