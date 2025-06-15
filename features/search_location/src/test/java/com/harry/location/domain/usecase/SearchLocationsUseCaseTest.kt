package com.harry.location.domain.usecase

import com.harry.location.domain.model.Location
import com.harry.location.domain.model.LocationSearchResult
import com.harry.location.domain.repository.LocationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchLocationsUseCaseTest {
    private val repository: LocationRepository = mockk()
    private val useCase = SearchLocationsUseCase(repository)

    private val testLocation =
        Location(
            name = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            country = "GB",
            state = "England",
        )

    private val testLocationSearchResult =
        LocationSearchResult(
            locations = listOf(testLocation),
            query = "London",
        )

    @Test
    fun `should return success when repository returns success`() =
        runTest {
            val query = "London"
            val expectedResult = Result.success(testLocationSearchResult)

            coEvery { repository.searchLocations(query) } returns expectedResult

            val result = useCase.invoke(query)

            assertEquals(expectedResult, result)
            assertTrue(result.isSuccess)
            assertEquals(testLocationSearchResult, result.getOrNull())
        }

    @Test
    fun `should return failure when repository returns failure`() =
        runTest {
            val query = "London"
            val exception = Exception("Network error")
            val expectedResult = Result.failure<LocationSearchResult>(exception)

            coEvery { repository.searchLocations(query) } returns expectedResult

            val result = useCase.invoke(query)

            assertEquals(expectedResult.exceptionOrNull()?.message, result.exceptionOrNull()?.message)
            assertTrue(result.isFailure)
        }

    @Test
    fun `should return failure when repository throws exception`() =
        runTest {
            val query = "London"
            val exception = RuntimeException("Repository error")

            coEvery { repository.searchLocations(query) } throws exception

            val result = useCase.invoke(query)

            assertTrue(result.isFailure)
            assertEquals("Repository error", result.exceptionOrNull()?.message)
        }

    @Test
    fun `should call repository with correct query`() =
        runTest {
            val query = "Paris"
            val expectedResult = Result.success(testLocationSearchResult)

            coEvery { repository.searchLocations(query) } returns expectedResult

            useCase.invoke(query)

            coVerify(exactly = 1) {
                repository.searchLocations(query)
            }
        }

    @Test
    fun `should handle empty query`() =
        runTest {
            val query = ""
            val emptyResult =
                LocationSearchResult(
                    locations = emptyList(),
                    query = query,
                )
            val expectedResult = Result.success(emptyResult)

            coEvery { repository.searchLocations(query) } returns expectedResult

            val result = useCase.invoke(query)

            assertTrue(result.isSuccess)
            assertEquals(emptyResult, result.getOrNull())
            coVerify(exactly = 1) {
                repository.searchLocations(query)
            }
        }

    @Test
    fun `should handle repository returning empty location list`() =
        runTest {
            val query = "NonexistentCity"
            val emptyResult =
                LocationSearchResult(
                    locations = emptyList(),
                    query = query,
                )
            val expectedResult = Result.success(emptyResult)

            coEvery { repository.searchLocations(query) } returns expectedResult

            val result = useCase.invoke(query)

            assertTrue(result.isSuccess)
            assertEquals(emptyResult, result.getOrNull())
            assertTrue(result.getOrNull()?.locations?.isEmpty() ?: false)
        }
}
