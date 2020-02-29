package com.lnicolet.currencyexchange.exchangelist.model

import androidx.annotation.DrawableRes

data class Currency(
    val currentValue: Double,
    @DrawableRes val flagResId: Int
)