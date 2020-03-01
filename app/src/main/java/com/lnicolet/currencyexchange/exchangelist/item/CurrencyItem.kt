package com.lnicolet.currencyexchange.exchangelist.item

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.lnicolet.currencyexchange.R
import com.lnicolet.currencyexchange.exchangelist.model.CurrencyExchange
import com.lnicolet.domain.model.CurrencyModel
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_currency.view.*

data class CurrencyItem(
    private var currencyExchange: CurrencyExchange,
    private val listener: CurrencyItemListener
) : Item<GroupieViewHolder>() {

    interface CurrencyItemListener {
        fun onCurrencyItemClicked(currencyExchange: CurrencyExchange)
        fun onCurrencyValueChanged(newValue: Double)
    }

    override fun getId() = this.currencyExchange.currencyModel.hashCode().toLong()

    override fun getLayout(): Int = R.layout.item_currency

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.root.apply {
            this.setOnClickListener {
                listener.onCurrencyItemClicked(this@CurrencyItem.currencyExchange)
            }
            currency_model_text.text = currencyExchange.currencyModel.name

            currency_value_conversion.setText(
                String.format("%.2f", currencyExchange.getExchangeConversion())
            )
            currency_flag.setImageDrawable(
                getDrawableByCurrencyModel(
                    this.context,
                    currencyExchange.currencyModel
                )
            )

            if (position == 0) {
                currency_value_conversion.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        listener.onCurrencyValueChanged(s?.toString()?.toDoubleOrNull() ?: 1.0)
                    }

                })
            }
        }
    }

    private fun getDrawableByCurrencyModel(
        context: Context,
        currencyModel: CurrencyModel
    ): Drawable? =
        ContextCompat.getDrawable(
            context,
            when (currencyModel) {
                CurrencyModel.AUD -> R.drawable.aud
                CurrencyModel.BGN -> R.drawable.bgn
                CurrencyModel.BRL -> R.drawable.brl
                CurrencyModel.CAD -> R.drawable.cad
                CurrencyModel.CHF -> R.drawable.chf
                CurrencyModel.CNY -> R.drawable.cny
                CurrencyModel.CZK -> R.drawable.czk
                CurrencyModel.DKK -> R.drawable.dkk
                CurrencyModel.EUR -> R.drawable.eur
                CurrencyModel.GBP -> R.drawable.gbp
                CurrencyModel.HKD -> R.drawable.hkd
                CurrencyModel.HRK -> R.drawable.hrk
                CurrencyModel.HUF -> R.drawable.huf
                CurrencyModel.IDR -> R.drawable.idr
                CurrencyModel.ILS -> R.drawable.ils
                CurrencyModel.INR -> R.drawable.inr
                CurrencyModel.ISK -> R.drawable.isk
                CurrencyModel.JPY -> R.drawable.jpy
                CurrencyModel.KRW -> R.drawable.krw
                CurrencyModel.MXN -> R.drawable.mxn
                CurrencyModel.MYR -> R.drawable.myr
                CurrencyModel.NOK -> R.drawable.nok
                CurrencyModel.NZD -> R.drawable.nzd
                CurrencyModel.PHP -> R.drawable.php
                CurrencyModel.PLN -> R.drawable.pln
                CurrencyModel.RON -> R.drawable.ron
                CurrencyModel.RUB -> R.drawable.rub
                CurrencyModel.SEK -> R.drawable.sek
                CurrencyModel.SGD -> R.drawable.sgd
                CurrencyModel.THB -> R.drawable.thb
                CurrencyModel.USD -> R.drawable.usd
                CurrencyModel.ZAR -> R.drawable.zar
            }
        )
}