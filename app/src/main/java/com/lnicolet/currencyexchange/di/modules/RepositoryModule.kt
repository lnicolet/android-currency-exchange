package com.lnicolet.currencyexchange.di.modules

import com.lnicolet.data.repository.CurrencyRepositoryImpl
import com.lnicolet.domain.repository.CurrencyRepository
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module(includes = [ApiModule::class])
abstract class RepositoryModule {

    @Binds
    abstract fun bindCurrencyRepository(repository: CurrencyRepositoryImpl): CurrencyRepository
}