package com.lnicolet.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lnicolet.data.utils.RxSchedulerRule
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.lnicolet.data.CurrencyAPI
import com.lnicolet.domain.repository.CurrencyRepository
import com.nhaarman.mockitokotlin2.any
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.lnicolet.data.entity.CurrencyEntity
import com.lnicolet.domain.model.CurrenciesExchangeModel
import com.lnicolet.domain.model.CurrencyModel
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.IllegalArgumentException
import kotlin.test.assertFailsWith

class CurrencyRepositoryTest {

    companion object {
        private const val CURRENCY_RESPONSE_OK =
            "{" +
                "\"baseCurrency\":\"EUR\"," +
                "\"rates\":{" +
                    "\"AUD\":1.612," +
                    "\"BGN\":1.972," +
                    "\"BRL\":4.2," +
                    "\"CAD\":1.505," +
                    "\"CHF\":1.15," +
                    "\"CNY\":7.666," +
                    "\"CZK\":25.735," +
                    "\"DKK\":7.519," +
                    "\"GBP\":0.89," +
                    "\"HKD\":8.968" +
                "}" +
             "}"
        private const val CURRENCY_RESPONSE_UNSUPPORTED =
            "{" +
                "\"baseCurrency\":\"EUR\"," +
                "\"rates\":{" +
                    "\"AUD\":1.612," +
                    "\"BGN\":1.972," +
                    "\"BRL\":4.2," +
                    "\"CIO\":1.505," +
                    "\"CHF\":1.15," +
                    "\"CNY\":7.666," +
                    "\"CZK\":25.735," +
                    "\"DKK\":7.519," +
                    "\"GBP\":0.89," +
                    "\"HKD\":8.968" +
                "}" +
             "}"
        private const val CURRENCY_RESPONSE_NON_OK = "{}"
    }

    @get:Rule
    val rxRule = RxSchedulerRule()
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()
    @Mock
    lateinit var lifeCycleOwner: LifecycleOwner
    @Mock
    lateinit var currencyAPI: CurrencyAPI
    private lateinit var repository: CurrencyRepository

    @Before
    fun `prepare for test`() {
        MockitoAnnotations.initMocks(this)

        repository = CurrencyRepositoryImpl(currencyAPI)
        setupLifeCycleOwner()
    }

    private fun setupLifeCycleOwner() {
        val lifecycle = LifecycleRegistry(lifeCycleOwner)
        Mockito.`when`(lifeCycleOwner.lifecycle).thenReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    @Test
    fun `verify that currencies are fetch correctly with OK response`() {
        val currencyType = object : TypeToken<CurrencyEntity>() {}.type
        val response = GsonBuilder().create().fromJson<CurrencyEntity>(
            CURRENCY_RESPONSE_OK, currencyType
        )

        Mockito.`when`(currencyAPI.currencyByBase(CurrencyModel.EUR.name))
            .thenReturn(Single.just(response))

        val observer = repository.getCurrencyByBase(CurrencyModel.EUR.name).test()
        observer.awaitTerminalEvent()

        observer
            .assertNoErrors()
            .assertComplete()
            .assertNoTimeout()
            .assertValue {
                it.baseCurrency == CurrencyModel.EUR
            }
    }

    @Test
    fun `verify that error is thrown with empty response`() {
        val currencyType = object : TypeToken<CurrencyEntity>() {}.type

        assertFailsWith(NullPointerException::class) {
            // checking deserializer throws NPE when there's no rates in the response
            GsonBuilder().create().fromJson<CurrencyEntity>(
                CURRENCY_RESPONSE_NON_OK, currencyType
            )
        }
    }

    @Test
    fun `verify that error is thrown upon unsupported currency`() {
        val currencyType = object : TypeToken<CurrencyEntity>() {}.type
        val response = GsonBuilder().create().fromJson<CurrencyEntity>(
            CURRENCY_RESPONSE_UNSUPPORTED, currencyType
        )

        Mockito.`when`(currencyAPI.currencyByBase(CurrencyModel.EUR.name))
            .thenReturn(Single.just(response))

        val observer = repository.getCurrencyByBase(CurrencyModel.EUR.name).test()
        observer.awaitTerminalEvent()

        observer
            .assertNoTimeout()
            .assertError {
                it is IllegalArgumentException
            }
    }
}