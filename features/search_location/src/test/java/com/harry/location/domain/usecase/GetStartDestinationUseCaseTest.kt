package com.harry.location.domain.usecase

import com.harry.location.domain.model.Location
import com.harry.location.domain.model.StartDestination
import com.harry.location.domain.repository.LocationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetStartDestinationUseCaseTest {
    private val repository: LocationRepository = mockk()
    private val useCase = GetStartDestinationUseCase(repository)

    private val testLocation = Location(
        name = "London",
        latitude = 51.5074,
        longitude = -0.1278,
        country = "United Kingdom"
    )

    @Test
    fun `should return WEATHER when stored location exists`() = runTest {
        coEvery { repository.getStoredLocation() } returns testLocation

        val result = useCase()

        assertEquals(StartDestination.WEATHER, result)
    }

    @Test
    fun `should return SEARCH_LOCATION when no stored location exists`() = runTest {
        coEvery { repository.getStoredLocation() } returns null

        val result = useCase()

        assertEquals(StartDestination.SEARCH_LOCATION, result)
    }
}