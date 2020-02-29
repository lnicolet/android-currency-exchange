package com.lnicolet.currencyexchange.exchangelist

import com.lnicolet.currencyexchange.core.BaseViewModel
import com.lnicolet.domain.model.CurrencyModel
import com.lnicolet.domain.usecase.CurrencyExchangeUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyExchangeViewModel @Inject constructor(
    private val currencyExchangeUseCase: CurrencyExchangeUseCase
): BaseViewModel() {

    init {
        lastDisposable = currencyExchangeUseCase.getCurrencyExchangeByBase(CurrencyModel.EUR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(1, TimeUnit.SECONDS)
            .repeat()
            .subscribe(
                {

                },
                {

                }
            )

    }

    override fun onCleared() {
        super.onCleared()
        disposeAll()
    }

}