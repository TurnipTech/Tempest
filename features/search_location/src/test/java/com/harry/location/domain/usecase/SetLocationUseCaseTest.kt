package com.harry.location.domain.usecase

import com.harry.location.domain.model.Location
import com.harry.location.domain.repository.LocationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SetLocationUseCaseTest {
    private val repository: LocationRepository = mockk()
    private val useCase = SetLocationUseCase(repository)

    private val testLocation =
        Location(
            name = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            country = "GB",
            state = "England",
        )

    @Test
    fun `should return success when repository sets location successfully`() =
        runTest {
            coEvery { repository.setLocation(testLocation) } returns Unit

            val result = useCase.invoke(testLocation)

            assertTrue(result.isSuccess)
            assertEquals(Unit, result.getOrNull())
        }

    @Test
    fun `should return failure when repository throws exception`() =
        runTest {
            val exception = RuntimeException("Storage error")

            coEvery { repository.setLocation(testLocation) } throws exception

            val result = useCase.invoke(testLocation)

            assertTrue(result.isFailure)
            assertEquals("Storage error", result.exceptionOrNull()?.message)
        }

    @Test
    fun `should call repository with correct location`() =
        runTest {
            coEvery { repository.setLocation(testLocation) } returns Unit

            useCase.invoke(testLocation)

            coVerify(exactly = 1) {
                repository.setLocation(testLocation)
            }
        }

    @Test
    fun `should handle different location types`() =
        runTest {
            val location =
                Location(
                    name = "Paris",
                    localNames = mapOf("fr" to "Paris", "en" to "Paris"),
                    latitude = 48.8566,
                    longitude = 2.3522,
                    country = "FR",
                    state = null,
                )

            coEvery { repository.setLocation(location) } returns Unit

            val result = useCase.invoke(location)

            assertTrue(result.isSuccess)
            coVerify(exactly = 1) {
                repository.setLocation(location)
            }
        }

    @Test
    fun `should handle location with local names map`() =
        runTest {
            val locationWithLocalNames =
                Location(
                    name = "Tokyo",
                    localNames = mapOf("ja" to "東京", "en" to "Tokyo"),
                    latitude = 35.6762,
                    longitude = 139.6503,
                    country = "JP",
                    state = "Tokyo",
                )

            coEvery { repository.setLocation(locationWithLocalNames) } returns Unit

            val result = useCase.invoke(locationWithLocalNames)

            assertTrue(result.isSuccess)
            assertEquals(Unit, result.getOrNull())
            coVerify(exactly = 1) {
                repository.setLocation(locationWithLocalNames)
            }
        }
}
