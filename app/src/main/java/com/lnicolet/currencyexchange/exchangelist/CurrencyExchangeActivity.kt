package com.lnicolet.currencyexchange.exchangelist

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lnicolet.currencyexchange.R
import com.lnicolet.currencyexchange.core.Status
import com.lnicolet.currencyexchange.core.gone
import com.lnicolet.currencyexchange.core.observe
import com.lnicolet.currencyexchange.core.visible
import com.lnicolet.currencyexchange.exchangelist.model.CurrencyExchangeViewState
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
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
        viewModel = viewModelFactory.create(CurrencyExchangeViewModel::class.java).apply {
            observe(this.currencyExchangeViewState, ::render)
        }
    }

    private fun render(currencyExchangeViewState: CurrencyExchangeViewState?) {
        when(currencyExchangeViewState?.status) {
            Status.Loading -> {
                loading.visible()
                recycler.gone()
            }
            is Status.Error<*> -> {
                loading.gone()
                recycler.gone()
            }
            is Status.Success<*> -> {
                loading.gone()
                recycler.visible()

            }
        }
    }
}
