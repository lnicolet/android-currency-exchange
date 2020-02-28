package com.lnicolet.currencyexchange

import androidx.multidex.MultiDexApplication
import com.lnicolet.currencyexchange.di.DaggerInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class CurrencyExchangeApp : MultiDexApplication(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        DaggerInjector
            .builder()
            .application(this)
            .build().inject(this)
    }

}