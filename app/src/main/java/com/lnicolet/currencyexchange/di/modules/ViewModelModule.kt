package com.lnicolet.currencyexchange.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lnicolet.currencyexchange.core.ViewModelFactory
import com.lnicolet.currencyexchange.core.ViewModelKey
import com.lnicolet.currencyexchange.exchangelist.CurrencyExchangeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(CurrencyExchangeViewModel::class)
    internal abstract fun bindCurrencyExchangeViewModel(viewModel: CurrencyExchangeViewModel): ViewModel
}