package com.lnicolet.domain.usecase

import com.lnicolet.domain.model.CurrenciesExchangeModel
import com.lnicolet.domain.model.CurrencyModel
import com.lnicolet.domain.repository.CurrencyRepository
import io.reactivex.Single
import javax.inject.Inject

class CurrencyExchangeUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    fun getCurrencyExchangeByBase(
        currency: CurrencyModel
    ): Single<CurrenciesExchangeModel> =
        repository.getCurrencyByBase(currency.name)
}