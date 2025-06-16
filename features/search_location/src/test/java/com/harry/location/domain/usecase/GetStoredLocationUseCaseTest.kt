package com.harry.location.domain.usecase

import com.harry.location.domain.model.Location
import com.harry.location.domain.repository.LocationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetStoredLocationUseCaseTest {
    private val repository: LocationRepository = mockk()
    private val useCase = GetStoredLocationUseCase(repository)

    private val testLocation =
        Location(
            name = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            country = "United Kingdom",
            state = "England",
            localNames = mapOf("en" to "London"),
        )

    @Test
    fun `should return stored location when repository has location`() =
        runTest {
            coEvery { repository.getStoredLocation() } returns testLocation

            val result = useCase()

            assertEquals(testLocation, result)
            coVerify(exactly = 1) { repository.getStoredLocation() }
        }

    @Test
    fun `should return null when repository has no stored location`() =
        runTest {
            coEvery { repository.getStoredLocation() } returns null

            val result = useCase()

            assertNull(result)
            coVerify(exactly = 1) { repository.getStoredLocation() }
        }

    @Test
    fun `should return location with minimal data when repository returns minimal location`() =
        runTest {
            val minimalLocation =
                Location(
                    name = "Paris",
                    latitude = 48.8566,
                    longitude = 2.3522,
                    country = "France",
                )
            coEvery { repository.getStoredLocation() } returns minimalLocation

            val result = useCase()

            assertEquals(minimalLocation, result)
            assertEquals("Paris", result?.name)
            assertEquals(48.8566, result?.latitude ?: 0.0, 0.001)
            assertEquals(2.3522, result?.longitude ?: 0.0, 0.001)
            assertEquals("France", result?.country)
            assertNull(result?.state)
            assertEquals(emptyMap<String, String>(), result?.localNames)
            coVerify(exactly = 1) { repository.getStoredLocation() }
        }
}
