package com.lnicolet.currencyexchange

import com.lnicolet.currencyexchange.core.BaseViewModel
import com.lnicolet.domain.model.CurrencyModel
import com.lnicolet.domain.usecase.CurrencyExchangeUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CurrencyExchangeViewModel @Inject constructor(
    private val currencyExchangeUseCase: CurrencyExchangeUseCase
): BaseViewModel() {

    init {
        lastDisposable = currencyExchangeUseCase.getCurrencyExchangeByBase(CurrencyModel.EUR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {

                },
                {

                }
            )

    }

}