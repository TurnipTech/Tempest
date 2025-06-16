package com.harry.location.data.repository

import com.harry.location.data.dto.GeocodingLocationDto
import com.harry.location.data.mapper.LocationMapper
import com.harry.location.domain.model.LocationSearchResult
import com.harry.network.client.HttpClient
import com.harry.storage.Storage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.KSerializer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationRepositoryImplTest {
    private val client: HttpClient = mockk(relaxed = true)
    private val mapper: LocationMapper = mockk(relaxed = true)
    private val storage: Storage = mockk(relaxed = true)
    private val apiKey = "test_api_key"

    private val repository =
        LocationRepositoryImpl(
            client = client,
            mapper = mapper,
            apiKey = apiKey,
            storage = storage,
        )

    private val testQuery = "London"
    private val testLatitude = 51.5074
    private val testLongitude = -0.1278
    private val testLimit = 5

    @Test
    fun `searchLocations calls client with correct endpoint and parameters`() =
        runTest {
            val mockDtos = listOf(mockk<GeocodingLocationDto>())
            val mockResult = mockk<LocationSearchResult>()
            val expectedParams =
                mapOf(
                    "q" to testQuery,
                    "limit" to testLimit.toString(),
                    "appid" to apiKey,
                )

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(mockDtos)
            every { mapper.mapToLocationSearchResult(mockDtos, testQuery) } returns mockResult

            val result = repository.searchLocations(testQuery, testLimit)

            coVerify {
                client.get(
                    any<KSerializer<List<GeocodingLocationDto>>>(),
                    endpoint = "https://api.openweathermap.org/geo/1.0/direct",
                    params = expectedParams,
                )
            }

            assertTrue(result.isSuccess)
            assertEquals(mockResult, result.getOrNull())
        }

    @Test
    fun `searchLocations calls mapper with correct parameters`() =
        runTest {
            val mockDtos = listOf(mockk<GeocodingLocationDto>())
            val mockResult = mockk<LocationSearchResult>()

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(mockDtos)
            every { mapper.mapToLocationSearchResult(mockDtos, testQuery) } returns mockResult

            repository.searchLocations(testQuery, testLimit)

            verify { mapper.mapToLocationSearchResult(mockDtos, testQuery) }
        }

    @Test
    fun `searchLocations uses default limit when not specified`() =
        runTest {
            val mockDtos = listOf(mockk<GeocodingLocationDto>())
            val expectedParams =
                mapOf(
                    "q" to testQuery,
                    "limit" to "5", // default limit
                    "appid" to apiKey,
                )

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(mockDtos)
            every { mapper.mapToLocationSearchResult(any(), any()) } returns mockk()

            repository.searchLocations(testQuery) // no limit parameter

            coVerify {
                client.get(
                    any<KSerializer<List<GeocodingLocationDto>>>(),
                    endpoint = any(),
                    params = expectedParams,
                )
            }
        }

    @Test
    fun `searchLocations handles client failure`() =
        runTest {
            val exception = RuntimeException("Network error")
            coEvery {
                client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any())
            } returns Result.failure(exception)

            val result = repository.searchLocations(testQuery, testLimit)

            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            verify(exactly = 0) { mapper.mapToLocationSearchResult(any(), any()) }
        }

    @Test
    fun `getLocationByCoordinates calls client with correct endpoint and parameters`() =
        runTest {
            val mockDtos = listOf(mockk<GeocodingLocationDto>())
            val mockResult = mockk<LocationSearchResult>()
            val expectedParams =
                mapOf(
                    "lat" to testLatitude.toString(),
                    "lon" to testLongitude.toString(),
                    "limit" to testLimit.toString(),
                    "appid" to apiKey,
                )
            val expectedCoordinateQuery = "$testLatitude,$testLongitude"

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(mockDtos)
            every { mapper.mapToLocationSearchResult(mockDtos, expectedCoordinateQuery) } returns mockResult

            val result = repository.getLocationByCoordinates(testLatitude, testLongitude, testLimit)

            coVerify {
                client.get(
                    any<KSerializer<List<GeocodingLocationDto>>>(),
                    endpoint = "https://api.openweathermap.org/geo/1.0/reverse",
                    params = expectedParams,
                )
            }

            assertTrue(result.isSuccess)
            assertEquals(mockResult, result.getOrNull())
        }

    @Test
    fun `getLocationByCoordinates calls mapper with coordinate query format`() =
        runTest {
            val mockDtos = listOf(mockk<GeocodingLocationDto>())
            val mockResult = mockk<LocationSearchResult>()
            val expectedCoordinateQuery = "$testLatitude,$testLongitude"

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(mockDtos)
            every { mapper.mapToLocationSearchResult(mockDtos, expectedCoordinateQuery) } returns mockResult

            repository.getLocationByCoordinates(testLatitude, testLongitude, testLimit)

            verify { mapper.mapToLocationSearchResult(mockDtos, expectedCoordinateQuery) }
        }

    @Test
    fun `getLocationByCoordinates uses default limit when not specified`() =
        runTest {
            val mockDtos = listOf(mockk<GeocodingLocationDto>())
            val expectedParams =
                mapOf(
                    "lat" to testLatitude.toString(),
                    "lon" to testLongitude.toString(),
                    "limit" to "1", // default limit for reverse geocoding
                    "appid" to apiKey,
                )

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(mockDtos)
            every { mapper.mapToLocationSearchResult(any(), any()) } returns mockk()

            repository.getLocationByCoordinates(testLatitude, testLongitude) // no limit parameter

            coVerify {
                client.get(
                    any<KSerializer<List<GeocodingLocationDto>>>(),
                    endpoint = any(),
                    params = expectedParams,
                )
            }
        }

    @Test
    fun `getLocationByCoordinates handles client failure`() =
        runTest {
            val exception = RuntimeException("Network error")
            coEvery {
                client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any())
            } returns Result.failure(exception)

            val result = repository.getLocationByCoordinates(testLatitude, testLongitude, testLimit)

            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            verify(exactly = 0) { mapper.mapToLocationSearchResult(any(), any()) }
        }

    @Test
    fun `searchLocations handles empty result list`() =
        runTest {
            val emptyDtos = emptyList<GeocodingLocationDto>()
            val mockResult = mockk<LocationSearchResult>()

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(emptyDtos)
            every { mapper.mapToLocationSearchResult(emptyDtos, testQuery) } returns mockResult

            val result = repository.searchLocations(testQuery, testLimit)

            assertTrue(result.isSuccess)
            assertEquals(mockResult, result.getOrNull())
            verify { mapper.mapToLocationSearchResult(emptyDtos, testQuery) }
        }

    @Test
    fun `getLocationByCoordinates handles empty result list`() =
        runTest {
            val emptyDtos = emptyList<GeocodingLocationDto>()
            val mockResult = mockk<LocationSearchResult>()
            val expectedCoordinateQuery = "$testLatitude,$testLongitude"

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(emptyDtos)
            every { mapper.mapToLocationSearchResult(emptyDtos, expectedCoordinateQuery) } returns mockResult

            val result = repository.getLocationByCoordinates(testLatitude, testLongitude, testLimit)

            assertTrue(result.isSuccess)
            assertEquals(mockResult, result.getOrNull())
            verify { mapper.mapToLocationSearchResult(emptyDtos, expectedCoordinateQuery) }
        }

    @Test
    fun `searchLocations with custom limit parameter`() =
        runTest {
            val customLimit = 10
            val mockDtos = listOf(mockk<GeocodingLocationDto>())
            val expectedParams =
                mapOf(
                    "q" to testQuery,
                    "limit" to customLimit.toString(),
                    "appid" to apiKey,
                )

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(mockDtos)
            every { mapper.mapToLocationSearchResult(any(), any()) } returns mockk()

            repository.searchLocations(testQuery, customLimit)

            coVerify {
                client.get(
                    any<KSerializer<List<GeocodingLocationDto>>>(),
                    endpoint = any(),
                    params = expectedParams,
                )
            }
        }

    @Test
    fun `getLocationByCoordinates with custom limit parameter`() =
        runTest {
            val customLimit = 3
            val mockDtos = listOf(mockk<GeocodingLocationDto>())
            val expectedParams =
                mapOf(
                    "lat" to testLatitude.toString(),
                    "lon" to testLongitude.toString(),
                    "limit" to customLimit.toString(),
                    "appid" to apiKey,
                )

            coEvery { client.get(any<KSerializer<List<GeocodingLocationDto>>>(), any(), any()) } returns
                Result.success(mockDtos)
            every { mapper.mapToLocationSearchResult(any(), any()) } returns mockk()

            repository.getLocationByCoordinates(testLatitude, testLongitude, customLimit)

            coVerify {
                client.get(
                    any<KSerializer<List<GeocodingLocationDto>>>(),
                    endpoint = any(),
                    params = expectedParams,
                )
            }
        }
}
