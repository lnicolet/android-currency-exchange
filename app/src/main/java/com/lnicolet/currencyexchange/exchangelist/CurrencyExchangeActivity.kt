package com.lnicolet.currencyexchange.exchangelist

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lnicolet.currencyexchange.R
import com.lnicolet.currencyexchange.core.gone
import com.lnicolet.currencyexchange.core.observe
import com.lnicolet.currencyexchange.core.visible
import com.lnicolet.currencyexchange.exchangelist.item.CurrencyItem
import com.lnicolet.currencyexchange.exchangelist.model.CurrencyExchange
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_currency_exchange.*
import javax.inject.Inject

class CurrencyExchangeActivity : DaggerAppCompatActivity(), CurrencyItem.CurrencyItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CurrencyExchangeViewModel
    private var groupAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_currency_exchange)
        setupViewModel()
        setupRecycler()
    }

    private fun setupRecycler() {
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler.adapter = groupAdapter
        recycler.setHasFixedSize(true)
    }

    private fun setupViewModel() {
        viewModel = viewModelFactory.create(CurrencyExchangeViewModel::class.java).apply {
            observe(this.currencyExchangeViewState, ::render)
        }
    }

    private fun render(currencyExchangeViewState: CurrencyExchangeViewState?) {
        when (currencyExchangeViewState) {
            CurrencyExchangeViewState.Loading -> {
                loading.visible()
                recycler.gone()
                bankruptError.gone()
            }
            is CurrencyExchangeViewState.Error<*> -> {
                loading.gone()
                recycler.gone()
                bankruptError.visible()
                Snackbar.make(root, R.string.something_went_wrong, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) {
                        viewModel.retryFailedApiCall()
                    }.show()
            }
            is CurrencyExchangeViewState.Success -> {
                loading.gone()
                bankruptError.gone()
                recycler.visible()
                val section = mutableListOf<CurrencyItem>()
                section.add(
                    0, CurrencyItem(
                        currencyExchangeViewState.firstResponder,
                        true,
                        this@CurrencyExchangeActivity
                    )
                )
                section.addAll(
                    currencyExchangeViewState.currencyExchangeList.map {
                        CurrencyItem(it, false, this@CurrencyExchangeActivity)
                    }
                )
                groupAdapter.update(section, true)
            }
        }
    }

    override fun onCurrencyItemClicked(currencyExchange: CurrencyExchange) {
        viewModel.updateFirstResponder(currencyExchange)
    }

    override fun onCurrencyValueChanged(newValue: Double) {
        viewModel.updateViewStateWithConversion(newValue)
    }
}
