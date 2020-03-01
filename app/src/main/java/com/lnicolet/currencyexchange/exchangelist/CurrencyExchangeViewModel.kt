package com.lnicolet.currencyexchange.exchangelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnicolet.currencyexchange.core.BaseViewModel
import com.lnicolet.currencyexchange.exchangelist.model.CurrencyExchange
import com.lnicolet.domain.model.CurrenciesExchangeModel
import com.lnicolet.domain.model.CurrencyModel
import com.lnicolet.domain.usecase.CurrencyExchangeUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyExchangeViewModel @Inject constructor(
    private val currencyExchangeUseCase: CurrencyExchangeUseCase
) : BaseViewModel() {

    private val _currencyExchangeViewState = MutableLiveData<CurrencyExchangeViewState>()
    val currencyExchangeViewState: LiveData<CurrencyExchangeViewState>
        get() = _currencyExchangeViewState

    private var firstResponder: CurrencyExchange = CurrencyExchange(CurrencyModel.EUR, 1.0, 100.0)

    init {
        _currencyExchangeViewState.postValue(CurrencyExchangeViewState.Loading)
        lastDisposable = setupCurrencyExchangeAPICall()
    }

    fun updateViewStateWithConversion(newValue: Double) {
        firstResponder.baseValue = newValue
        _currencyExchangeViewState.postValue(
            _currencyExchangeViewState.value.apply {
                if (this is CurrencyExchangeViewState.Success) {
                    this.value.map {
                        CurrencyExchange(
                            it.currencyModel,
                            it.exchangeRate,
                            newValue
                        )
                    }
                }
            }
        )
    }

    fun updateFirstResponder(currencyExchange: CurrencyExchange) {
        lastDisposable?.dispose()
        firstResponder = currencyExchange.apply {
            baseValue = exchangeRate * currencyExchange.baseValue
            exchangeRate = 1.0
        }
        lastDisposable = setupCurrencyExchangeAPICall()
    }

    private fun setupCurrencyExchangeAPICall(): Disposable? =
        currencyExchangeUseCase.getCurrencyExchangeByBase(firstResponder.currencyModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(1, TimeUnit.SECONDS)
            .repeat()
            .subscribe(::onGetCurrencyExchangeByBaseSucceeded, ::onGetCurrencyExchangeByBaseFailed)

    private fun onGetCurrencyExchangeByBaseSucceeded(exchangeModel: CurrenciesExchangeModel) {
        val currencyList = exchangeModel.rates.map { rateModel ->
            CurrencyExchange(
                rateModel.currency,
                rateModel.rate,
                firstResponder.baseValue
            )
        }.sortedBy {
            it.currencyModel.name
        }.toMutableList()

        _currencyExchangeViewState.postValue(
            CurrencyExchangeViewState.Success(
                firstResponder,
                currencyList
            )
        )
    }

    private fun onGetCurrencyExchangeByBaseFailed(exception: Throwable) {
        _currencyExchangeViewState.postValue(
            CurrencyExchangeViewState.Error(exception)
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
    data class Success(
        var firstResponder: CurrencyExchange,
        val value: MutableList<CurrencyExchange>
    ) : CurrencyExchangeViewState()
}