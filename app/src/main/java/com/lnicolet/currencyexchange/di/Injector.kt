package com.lnicolet.currencyexchange.di

import android.app.Application
import com.lnicolet.currencyexchange.CurrencyExchangeApp
import com.lnicolet.currencyexchange.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Suppress("unused")
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        AppModule::class,
        ApiModule::class,
        RepositoryModule::class
    ]
)
interface Injector : AndroidInjector<CurrencyExchangeApp> {
    @Component.Builder
    interface Builder {
        fun build(): Injector

        @BindsInstance
        fun application(application: Application): Builder
    }

    fun inject(application: Application)
}