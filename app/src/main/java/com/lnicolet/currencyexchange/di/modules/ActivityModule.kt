package com.lnicolet.currencyexchange.di.modules

import com.lnicolet.currencyexchange.CurrencyExchangeActivity
import com.lnicolet.currencyexchange.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributesCurrencyExchangeActivityInjector(): CurrencyExchangeActivity

}