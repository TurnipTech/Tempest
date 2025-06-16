@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.harry.tempest

import com.harry.location.domain.model.Location
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
    fun `should set start destination to Weather when use case returns Weather`() =
        runTest {
            val location =
                Location(
                    name = "Test City",
                    latitude = 53.8,
                    longitude = 1.76,
                    country = "GB",
                )
            coEvery { getStartDestinationUseCase() } returns StartDestination.Weather(location)

            val viewModel = TempestViewModel(getStartDestinationUseCase)
            advanceUntilIdle()

            assertEquals(StartDestination.Weather(location), viewModel.startDestination.value)
        }

    @Test
    fun `should set start destination to SearchLocation when use case returns SearchLocation`() =
        runTest {
            coEvery { getStartDestinationUseCase() } returns StartDestination.SearchLocation

            val viewModel = TempestViewModel(getStartDestinationUseCase)
            advanceUntilIdle()

            assertEquals(StartDestination.SearchLocation, viewModel.startDestination.value)
        }
}
