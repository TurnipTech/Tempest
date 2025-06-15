@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.harry.tempest

import com.harry.location.domain.model.StartDestination
import com.harry.location.domain.usecase.GetStartDestinationUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TempestViewModelTest {
    private val getStartDestinationUseCase: GetStartDestinationUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `should set start destination to WEATHER when use case returns WEATHER`() = runTest {
        coEvery { getStartDestinationUseCase() } returns StartDestination.WEATHER

        val viewModel = TempestViewModel(getStartDestinationUseCase)
        advanceUntilIdle()

        assertEquals(StartDestination.WEATHER, viewModel.startDestination.value)
    }

    @Test
    fun `should set start destination to SEARCH_LOCATION when use case returns SEARCH_LOCATION`() = runTest {
        coEvery { getStartDestinationUseCase() } returns StartDestination.SEARCH_LOCATION

        val viewModel = TempestViewModel(getStartDestinationUseCase)
        advanceUntilIdle()

        assertEquals(StartDestination.SEARCH_LOCATION, viewModel.startDestination.value)
    }
}