package com.lnicolet.currencyexchange.di.modules

import com.lnicolet.data.CurrencyAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Suppress("unused")
@Module(includes = [NetworkModule::class])
class ApiModule {

    @Provides
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyAPI =
        retrofit.create(CurrencyAPI::class.java)
}