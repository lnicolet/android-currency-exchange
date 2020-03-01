package com.lnicolet.currencyexchange.core

sealed class Status<T> {
    object Loading : Status<Any>()
    data class Error<E>(val reason: E) : Status<E>()
    data class Success<E>(val value: E) : Status<E>()
}