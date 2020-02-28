package com.lnicolet.data

import com.lnicolet.data.entity.CurrencyEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("latest/")
    fun currencyByBase(@Query("base") baseCurrencyString: String): Single<CurrencyEntity>

}