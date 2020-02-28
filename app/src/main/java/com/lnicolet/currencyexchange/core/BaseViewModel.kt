package com.lnicolet.currencyexchange.core

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseViewModel : ViewModel() {

    internal var lastDisposable: Disposable? = null
    internal val disposables = CompositeDisposable()

    fun disposeLast() {
        lastDisposable?.let { disposable ->
            if (!disposable.isDisposed)
                disposable.dispose()
        }
    }

    fun disposeAll() {
        disposables.clear()
    }
}