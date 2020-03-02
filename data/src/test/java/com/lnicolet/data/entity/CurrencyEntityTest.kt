package com.lnicolet.data.entity

import com.lnicolet.domain.model.CurrencyModel
import com.lnicolet.domain.model.RateModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import kotlin.test.assertFailsWith

class CurrencyEntityTest {
    companion object {
        private const val BASE_CURRENCY = "EUR"
        private val EMPTY_LIST = emptyList<RateEntity>()
        private val LIST_OF_RATES = listOf(
            RateEntity("GBP", 1.19),
            RateEntity("BRL", 18.99),
            RateEntity("AUD", 1.59)
        )
        private val LIST_WITH_INVALID_RATES = listOf(
            RateEntity("GKP", 1.19),
            RateEntity("BRL", 18.99),
            RateEntity("AUD", 1.59)
        )
        private const val INVALID_CURRENCY = "EUL"
    }

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var currencyEntity: CurrencyEntity

    @Before
    fun `prepare for test`() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `verify exception is thrown when unsupported currency is returned`() {
        currencyEntity = CurrencyEntity(INVALID_CURRENCY, EMPTY_LIST)
        assertFailsWith(IllegalArgumentException::class) {
            currencyEntity.toDomain()
        }
    }

    @Test
    fun `verify empty list is returned`() {
        currencyEntity = CurrencyEntity(BASE_CURRENCY, EMPTY_LIST)
        val model = currencyEntity.toDomain()

        assert(model.rates.isEmpty())
    }

    @Test
    fun `verify list is mapped correctly`() {
        currencyEntity = CurrencyEntity(BASE_CURRENCY, LIST_OF_RATES)
        val model = currencyEntity.toDomain()

        assert(model.rates.size == 3)
        assert(model.rates.contains(RateModel(CurrencyModel.GBP, 1.19)))
    }

    @Test
    fun `verify exception is thrown when unsupported currency is returned in list`() {
        currencyEntity = CurrencyEntity(BASE_CURRENCY, LIST_WITH_INVALID_RATES)
        assertFailsWith(IllegalArgumentException::class) {
            currencyEntity.toDomain()
        }
    }
}