package com.lnicolet.currencyexchange.exchangelist

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lnicolet.currencyexchange.R
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class CurrencyExchangeActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CurrencyExchangeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = viewModelFactory.create(CurrencyExchangeViewModel::class.java)
    }
}
