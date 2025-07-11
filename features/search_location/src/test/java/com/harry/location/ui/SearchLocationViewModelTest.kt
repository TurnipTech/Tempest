@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.harry.location.ui

import com.harry.location.R
import com.harry.location.domain.model.Location
import com.harry.location.domain.model.LocationSearchResult
import com.harry.location.domain.usecase.SearchLocationsUseCase
import com.harry.location.domain.usecase.SetLocationUseCase
import com.harry.location.ui.mapper.SearchLocationUiMapper
import com.harry.location.ui.model.SearchLocationUiState
import com.harry.location.ui.model.SearchResult
import com.harry.utils.ResourceProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchLocationViewModelTest {
    private val searchLocationsUseCase: SearchLocationsUseCase = mockk(relaxed = true)
    private val setLocationUseCase: SetLocationUseCase = mockk(relaxed = true)
    private val searchLocationUiMapper: SearchLocationUiMapper = mockk(relaxed = true)
    private val mockResourceProvider =
        mockk<ResourceProvider> {
            every { getString(R.string.error_unknown) } returns "Unknown error occurred"
        }
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    private val testLocation =
        Location(
            name = "London",
            latitude = 51.5074,
            longitude = -0.1278,
            country = "GB",
            state = "England",
        )

    private val testSearchResult =
        SearchResult(
            displayName = "London, England, GB",
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

    private val viewModel =
        SearchLocationViewModel(
            searchLocationsUseCase = searchLocationsUseCase,
            setLocationUseCase = setLocationUseCase,
            searchLocationUiMapper = searchLocationUiMapper,
            resourceProvider = mockResourceProvider,
        )

    @Test
    fun `initial state should be Idle`() {
        assertEquals(SearchLocationUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `should be in Idle state when query is blank`() =
        runTest {
            viewModel.searchLocations("")

            assertEquals(SearchLocationUiState.Idle, viewModel.uiState.value)
        }

    @Test
    fun `should be in Idle state when query is less than 2 characters`() =
        runTest {
            viewModel.searchLocations("L")

            assertEquals(SearchLocationUiState.Idle, viewModel.uiState.value)
        }

    @Test
    fun `should debounce search queries properly`() =
        runTest {
            val query = "London"

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(testLocationSearchResult)
            every { searchLocationUiMapper.mapToSearchResults(any()) } returns listOf(testSearchResult)

            viewModel.searchLocations(query)
            advanceTimeBy(200) // Less than debounce time

            // Should not have called the use case yet
            coVerify(exactly = 0) {
                searchLocationsUseCase.invoke(any())
            }

            advanceTimeBy(200) // Complete debounce time

            // Now the search should have been triggered
            coVerify(exactly = 1) {
                searchLocationsUseCase.invoke(query)
            }
        }

    @Test
    fun `should be in Success state when search succeeds`() =
        runTest {
            val query = "London"
            val expectedState =
                SearchLocationUiState.Success(
                    locations = listOf(testSearchResult),
                    query = query,
                )

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(testLocationSearchResult)
            every { searchLocationUiMapper.mapToSearchResults(listOf(testLocation)) } returns listOf(testSearchResult)

            viewModel.searchLocations(query)
            advanceUntilIdle()

            assertEquals(expectedState, viewModel.uiState.value)
        }

    @Test
    fun `should be in Error state when search fails`() =
        runTest {
            val query = "London"
            val errorMessage = "Network error"
            val expectedState =
                SearchLocationUiState.Error(
                    message = errorMessage,
                    query = query,
                )

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.failure(Exception(errorMessage))

            viewModel.searchLocations(query)
            advanceUntilIdle()

            assertEquals(expectedState, viewModel.uiState.value)
        }

    @Test
    fun `should be in Error state with default message when exception message is null`() =
        runTest {
            val query = "London"
            val expectedState =
                SearchLocationUiState.Error(
                    message = "Unknown error occurred",
                    query = query,
                )

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.failure(Exception())

            viewModel.searchLocations(query)
            advanceUntilIdle()

            assertEquals(expectedState, viewModel.uiState.value)
        }

    @Test
    fun `should call usecase with correct query`() =
        runTest {
            val query = "Paris"

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(testLocationSearchResult)
            every { searchLocationUiMapper.mapToSearchResults(any()) } returns listOf(testSearchResult)

            viewModel.searchLocations(query)
            advanceUntilIdle()

            coVerify(exactly = 1) {
                searchLocationsUseCase.invoke(query)
            }
        }

    @Test
    fun `should call mapper when search succeeds`() =
        runTest {
            val query = "London"

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(testLocationSearchResult)
            every { searchLocationUiMapper.mapToSearchResults(listOf(testLocation)) } returns listOf(testSearchResult)

            viewModel.searchLocations(query)
            advanceUntilIdle()

            verify(exactly = 1) {
                searchLocationUiMapper.mapToSearchResults(listOf(testLocation))
            }
        }

    @Test
    fun `should not call mapper when search fails`() =
        runTest {
            val query = "London"

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.failure(Exception("Error"))

            viewModel.searchLocations(query)
            advanceUntilIdle()

            verify(exactly = 0) {
                searchLocationUiMapper.mapToSearchResults(any())
            }
        }

    @Test
    fun `should cancel previous search when new search is initiated`() =
        runTest {
            val firstQuery = "Lond"
            val secondQuery = "London"

            coEvery { searchLocationsUseCase.invoke(firstQuery) } coAnswers {
                delay(500)
                Result.success(testLocationSearchResult)
            }
            coEvery { searchLocationsUseCase.invoke(secondQuery) } returns Result.success(testLocationSearchResult)
            every { searchLocationUiMapper.mapToSearchResults(any()) } returns listOf(testSearchResult)

            viewModel.searchLocations(firstQuery)
            advanceTimeBy(200) // Partial debounce time

            viewModel.searchLocations(secondQuery)
            advanceUntilIdle()

            // Only the second search should complete due to flatMapLatest cancelling the first
            coVerify(exactly = 1) {
                searchLocationsUseCase.invoke(secondQuery)
            }
            coVerify(exactly = 0) {
                searchLocationsUseCase.invoke(firstQuery)
            }
        }

    @Test
    fun `should wait for debounce before searching`() =
        runTest {
            val query = "London"

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(testLocationSearchResult)

            viewModel.searchLocations(query)
            advanceTimeBy(200) // Less than debounce time

            // Should not have called the use case yet
            coVerify(exactly = 0) {
                searchLocationsUseCase.invoke(any())
            }

            advanceTimeBy(200) // Complete debounce time

            coVerify(exactly = 1) {
                searchLocationsUseCase.invoke(query)
            }
        }

    @Test
    fun `clearSearch should set state to Idle`() =
        runTest {
            val query = "London"

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(testLocationSearchResult)
            every { searchLocationUiMapper.mapToSearchResults(any()) } returns listOf(testSearchResult)

            viewModel.searchLocations(query)
            advanceUntilIdle()

            // Verify we're in success state first
            assertEquals(SearchLocationUiState.Success::class, viewModel.uiState.value::class)

            viewModel.clearSearch()

            assertEquals(SearchLocationUiState.Idle, viewModel.uiState.value)
        }

    @Test
    fun `should handle empty location list in success result`() =
        runTest {
            val query = "NonexistentCity"
            val emptySearchResult =
                LocationSearchResult(
                    locations = emptyList(),
                    query = query,
                )
            val expectedState =
                SearchLocationUiState.Success(
                    locations = emptyList(),
                    query = query,
                )

            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(emptySearchResult)
            every { searchLocationUiMapper.mapToSearchResults(emptyList()) } returns emptyList()

            viewModel.searchLocations(query)
            advanceUntilIdle()

            assertEquals(expectedState, viewModel.uiState.value)
        }

    @Test
    fun `onLocationSelected should navigate to weather when location is stored successfully`() =
        runTest {
            every { searchLocationUiMapper.mapToLocation(testSearchResult) } returns testLocation
            coEvery { setLocationUseCase(testLocation) } returns Result.success(Unit)

            viewModel.onLocationSelected(testSearchResult)
            advanceUntilIdle()

            val expectedState = SearchLocationUiState.NavigateToWeather(testLocation)
            assertEquals(expectedState, viewModel.uiState.value)
            coVerify(exactly = 1) { setLocationUseCase(testLocation) }
        }

    @Test
    fun `onLocationSelected should not navigate when location storage fails`() =
        runTest {
            every { searchLocationUiMapper.mapToLocation(testSearchResult) } returns testLocation
            coEvery { setLocationUseCase(testLocation) } returns Result.failure(Exception("Storage failed"))

            val initialState = viewModel.uiState.value
            viewModel.onLocationSelected(testSearchResult)
            advanceUntilIdle()

            assertEquals(initialState, viewModel.uiState.value)
            coVerify(exactly = 1) { setLocationUseCase(testLocation) }
        }

    @Test
    fun `retrySearch should call searchLocations with current query when in Error state`() =
        runTest {
            val query = "London"
            val errorMessage = "Network error"

            // First search fails
            coEvery { searchLocationsUseCase.invoke(query) } returns Result.failure(Exception(errorMessage))
            viewModel.searchLocations(query)
            advanceUntilIdle()

            // Verify we're in error state
            val errorState = viewModel.uiState.value as SearchLocationUiState.Error
            assertEquals(query, errorState.query)
            assertEquals(errorMessage, errorState.message)

            // Setup mock for retry
            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(testLocationSearchResult)
            every { searchLocationUiMapper.mapToSearchResults(listOf(testLocation)) } returns listOf(testSearchResult)

            // Perform retry
            viewModel.retrySearch()
            advanceUntilIdle()

            // Verify use case was called with the correct query (should be 2 total: original + retry)
            coVerify(exactly = 2) {
                searchLocationsUseCase.invoke(query)
            }
        }

    @Test
    fun `retrySearch should do nothing when not in Error state`() =
        runTest {
            val query = "London"

            // Set up successful search
            coEvery { searchLocationsUseCase.invoke(query) } returns Result.success(testLocationSearchResult)
            every { searchLocationUiMapper.mapToSearchResults(any()) } returns listOf(testSearchResult)

            viewModel.searchLocations(query)
            advanceUntilIdle()

            // Verify we're in success state
            assertEquals(SearchLocationUiState.Success::class, viewModel.uiState.value::class)

            // Call retry when not in error state
            viewModel.retrySearch()
            advanceUntilIdle()

            // State should remain unchanged
            assertEquals(SearchLocationUiState.Success::class, viewModel.uiState.value::class)

            // Use case should only have been called once (no retry)
            coVerify(exactly = 1) {
                searchLocationsUseCase.invoke(query)
            }
        }

    @Test
    fun `retrySearch should handle retry failure correctly`() =
        runTest {
            val query = "London"
            val originalError = "Network error"
            val retryError = "Retry failed"

            // Setup sequential mock responses: first fail, then fail again with different error
            coEvery { searchLocationsUseCase.invoke(query) } returnsMany
                listOf(
                    Result.failure(Exception(originalError)),
                    Result.failure(Exception(retryError)),
                )

            // First search fails
            viewModel.searchLocations(query)
            advanceUntilIdle()

            // Verify we're in error state
            val errorState = viewModel.uiState.value as SearchLocationUiState.Error
            assertEquals(originalError, errorState.message)

            // Perform retry
            viewModel.retrySearch()
            advanceUntilIdle()

            // Verify retry failed with new error message
            val retryErrorState = viewModel.uiState.value as SearchLocationUiState.Error
            assertEquals(query, retryErrorState.query)
            assertEquals(retryError, retryErrorState.message)

            // Verify use case was called twice (original + retry)
            coVerify(exactly = 2) {
                searchLocationsUseCase.invoke(query)
            }
        }
}
