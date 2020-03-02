package com.lnicolet.data.entity

import com.lnicolet.domain.model.CurrencyModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import kotlin.test.assertFailsWith

class RateEntityTest {

    companion object {
        private const val CURRENCY = "EUR"
        private const val VALUE = 1.0
        private const val INVALID_CURRENCY = "EUL"
    }

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var rateEntity: RateEntity

    @Before
    fun `prepare for test`() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `verify rate is mapped correctly`() {
        rateEntity = RateEntity(CURRENCY, VALUE)
        val model = rateEntity.toDomain()

        assert(model.currency == CurrencyModel.EUR)
        assert(model.rate == VALUE)
    }

    @Test
    fun `verify exception is thrown for unsupported currency`() {
        rateEntity = RateEntity(INVALID_CURRENCY, VALUE)
        assertFailsWith(IllegalArgumentException::class) {
            rateEntity.toDomain()
        }
    }

}