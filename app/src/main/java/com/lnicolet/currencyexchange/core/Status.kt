package com.lnicolet.currencyexchange.core

sealed class Status {
    object Loading: Status()
    data class Error<E>(val reason: E): Status()
    data class Success<E>(val value: E): Status()
}