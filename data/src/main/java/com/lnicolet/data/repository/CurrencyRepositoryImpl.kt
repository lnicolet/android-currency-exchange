package com.lnicolet.data.repository

import com.lnicolet.data.CurrencyAPI
import com.lnicolet.domain.model.CurrenciesExchangeModel
import com.lnicolet.domain.repository.CurrencyRepository
import io.reactivex.Single
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyAPI: CurrencyAPI
) : CurrencyRepository {

    override fun getCurrencyByBase(base: String): Single<CurrenciesExchangeModel> {
        return currencyAPI
            .currencyByBase(base)
            .map {
                it.toDomain()
            }
    }
}