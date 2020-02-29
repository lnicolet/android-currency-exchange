package com.lnicolet.currencyexchange.exchangelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnicolet.currencyexchange.core.BaseViewModel
import com.lnicolet.currencyexchange.exchangelist.model.CurrencyExchange
import com.lnicolet.domain.model.CurrencyModel
import com.lnicolet.domain.usecase.CurrencyExchangeUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyExchangeViewModel @Inject constructor(
    private val currencyExchangeUseCase: CurrencyExchangeUseCase
) : BaseViewModel() {

    private val _currencyExchangeViewState = MutableLiveData<CurrencyExchangeViewState>()
    val currencyExchangeViewState: LiveData<CurrencyExchangeViewState>
        get() = _currencyExchangeViewState

    private var exchangeConversionValue: Double = 1.0

    fun updateViewStateWithConversion(newValue: Double) {
        exchangeConversionValue = newValue
        _currencyExchangeViewState.postValue(
            _currencyExchangeViewState.value.apply {
                if (this is CurrencyExchangeViewState.Success) {
                    this.value.map {
                        CurrencyExchange(
                            it.currencyModel,
                            it.exchangeRate,
                            it.exchangeConversion * exchangeConversionValue
                        )
                    }
                }
            }
        )
    }

    init {
        _currencyExchangeViewState.postValue(CurrencyExchangeViewState.Loading)
        lastDisposable = currencyExchangeUseCase.getCurrencyExchangeByBase(CurrencyModel.EUR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(1, TimeUnit.SECONDS)
            .repeat()
            .subscribe(
                { exchangeModel ->
                    val currencyList = exchangeModel.rates.map { rateModel ->
                        CurrencyExchange(
                            rateModel.currency,
                            rateModel.value,
                            exchangeConversionValue * rateModel.value
                        )
                    }.toMutableList()

                    currencyList.add(0, CurrencyExchange(exchangeModel.baseCurrency, 1.0, 1.0))
                    _currencyExchangeViewState.postValue(
                        CurrencyExchangeViewState.Success(currencyList)
                    )
                },
                {
                    _currencyExchangeViewState.postValue(
                        CurrencyExchangeViewState.Error(it)
                    )
                }
            )

    }

    override fun onCleared() {
        super.onCleared()
        disposeAll()
    }

}


sealed class CurrencyExchangeViewState {
    object Loading : CurrencyExchangeViewState()
    data class Error<E>(val reason: E) : CurrencyExchangeViewState()
    data class Success(val value: List<CurrencyExchange>) : CurrencyExchangeViewState()
}