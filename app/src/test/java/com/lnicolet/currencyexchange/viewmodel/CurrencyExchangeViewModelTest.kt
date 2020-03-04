package com.lnicolet.currencyexchange.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.lnicolet.currencyexchange.exchangelist.CurrencyExchangeViewModel
import com.lnicolet.currencyexchange.exchangelist.CurrencyExchangeViewState
import com.lnicolet.currencyexchange.exchangelist.model.CurrencyExchange
import com.lnicolet.currencyexchange.utils.RxSchedulerRule
import com.lnicolet.domain.model.CurrenciesExchangeModel
import com.lnicolet.domain.model.CurrencyModel
import com.lnicolet.domain.model.RateModel
import com.lnicolet.domain.repository.CurrencyRepository
import com.lnicolet.domain.usecase.CurrencyExchangeUseCase
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner
import java.util.concurrent.TimeUnit


@RunWith(PowerMockRunner::class)
class CurrencyExchangeViewModelTest {

    // Forces RxJava to execute on a specified thread for tests
    @Rule
    val rxRule = RxSchedulerRule()
    @Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    lateinit var currencyRepo: CurrencyRepository

    @Mock
    lateinit var postDetailsViewStateObserver: Observer<CurrencyExchangeViewState>

    lateinit var currencyUseCase: CurrencyExchangeUseCase
    private lateinit var currencyExchangeViewModel: CurrencyExchangeViewModel

    @Before
    fun `prepare for test`() {
        MockitoAnnotations.initMocks(this)
        setupLifecycleOwner()
    }

    @Test
    fun `verify that error response create correct view state`() {
        val error = Throwable("")
        // Arrange
        Mockito.`when`(currencyRepo.getCurrencyByBase("EUR"))
            .thenReturn(Single.error(error))

        // Act: view model init fires api call
        setupViewModel()

        // Assert
        Mockito.verify(postDetailsViewStateObserver)
            .onChanged(
                CurrencyExchangeViewState.Error(error)
            )
    }

    @Test
    fun `verify that correct response create correct view state`() {
        // This test fails. The only way I found to have this test pass is commenting the `.repeat()` on the ViewModel in order to not end up in an endless loop.
        val firstResponder = CurrencyExchange(CurrencyModel.EUR, 1.0, 100.0)
        val currencyList = listOf(
            RateModel(CurrencyModel.AUD, 1.59),
            RateModel(CurrencyModel.CHF, 0.88),
            RateModel(CurrencyModel.GBP, 1.19)
        )

        // Arrange
        Mockito.doReturn(Single.just(CurrenciesExchangeModel(CurrencyModel.EUR, currencyList)))
            .`when`(currencyRepo).getCurrencyByBase("EUR")

        val scheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }

        // Act: view model init fires api call
        setupViewModel()

        scheduler.advanceTimeBy(1L, TimeUnit.SECONDS)

        // Assert
        Mockito.verify(postDetailsViewStateObserver)
            .onChanged(
                CurrencyExchangeViewState.Success(
                    firstResponder = firstResponder,
                    currencyExchangeList = currencyList.map {
                        CurrencyExchange(it.currency, it.rate, firstResponder.baseValue)
                    }.toMutableList()
                )
            )
    }

    @Test
    fun `verify that updating current value cause list to update value `() {
        // This test fails. The only way I found to have this test pass is commenting the `.repeat()` on the ViewModel in order to not end up in an endless loop.
        val firstResponder = CurrencyExchange(CurrencyModel.EUR, 1.0, 100.0)
        val currencyList = listOf(
            RateModel(CurrencyModel.AUD, 1.59),
            RateModel(CurrencyModel.CHF, 0.88),
            RateModel(CurrencyModel.GBP, 1.19)
        )

        // Arrange
        Mockito.doReturn(Single.just(CurrenciesExchangeModel(CurrencyModel.EUR, currencyList)))
            .`when`(currencyRepo).getCurrencyByBase("EUR")

        val scheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }

        // Act: view model init fires api call
        setupViewModel()
        scheduler.advanceTimeBy(1L, TimeUnit.SECONDS)

        // Assert
        Mockito.verify(postDetailsViewStateObserver)
            .onChanged(
                CurrencyExchangeViewState.Success(
                    firstResponder = firstResponder,
                    currencyExchangeList = currencyList.map {
                        CurrencyExchange(it.currency, it.rate, firstResponder.baseValue)
                    }.toMutableList()
                )
            )
        // Act 2
        currencyExchangeViewModel.updateViewStateWithConversion(15.0)
        scheduler.advanceTimeBy(1L, TimeUnit.SECONDS)

        // Assert
        Mockito.verify(postDetailsViewStateObserver)
            .onChanged(
                CurrencyExchangeViewState.Success(
                    firstResponder = firstResponder.apply { baseValue = 15.0 },
                    currencyExchangeList = currencyList.map {
                        CurrencyExchange(it.currency, it.rate, 15.0)
                    }.toMutableList()
                )
            )
    }

    private fun setupViewModel() {
        currencyUseCase = CurrencyExchangeUseCase(currencyRepo)
        currencyExchangeViewModel = CurrencyExchangeViewModel(currencyUseCase)
        setupObservers()
    }

    private fun setupObservers() {
        currencyExchangeViewModel.currencyExchangeViewState.observe(
            lifecycleOwner,
            postDetailsViewStateObserver
        )
    }

    private fun setupLifecycleOwner() {
        val lifecycle = LifecycleRegistry(lifecycleOwner)
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }
}