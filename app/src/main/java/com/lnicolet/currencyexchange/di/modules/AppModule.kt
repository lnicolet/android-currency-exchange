package com.lnicolet.currencyexchange.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideAppContext(app: Application): Context = app.applicationContext
}