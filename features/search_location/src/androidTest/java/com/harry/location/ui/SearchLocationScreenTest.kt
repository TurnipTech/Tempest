package com.harry.location.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.harry.design.TempestTheme
import com.harry.location.domain.model.Location
import com.harry.location.ui.model.SearchLocationUiState
import com.harry.location.ui.model.SearchResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class SearchLocationScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchLocationScreen_idleState_displaysWelcomeMessage() {
        SearchLocationScreenRobot(composeTestRule).apply {
            setIdleState()
            assertWelcomeMessageIsDisplayed()
        }
    }

    @Test
    fun searchLocationScreen_loadingState_afterTyping_displaysLoadingIndicator() {
        SearchLocationScreenRobot(composeTestRule).apply {
            setLoadingState()
            typeInSearchField("London")
            assertLoadingIndicatorIsDisplayed()
        }
    }

    @Test
    fun searchLocationScreen_successState_afterTyping_displaysSearchResults() {
        SearchLocationScreenRobot(composeTestRule).apply {
            setSuccessState()
            typeInSearchField("London")
            assertSearchResultsAreDisplayed()
        }
    }

    @Test
    fun searchLocationScreen_successStateWithEmptyResults_afterTyping_displaysEmptyState() {
        SearchLocationScreenRobot(composeTestRule).apply {
            setSuccessStateWithEmptyResults()
            typeInSearchField("xyz")
            assertEmptyStateIsDisplayed()
        }
    }

    @Test
    fun searchLocationScreen_errorState_afterTyping_doesNotDisplayEmptyState() {
        val errorMessage = "Network connection failed"
        SearchLocationScreenRobot(composeTestRule).apply {
            setErrorState(errorMessage)
            typeInSearchField("test")
            assertEmptyStateIsNotDisplayed()
        }
    }

    @Test
    fun searchLocationScreen_searchInput_triggersSearch() {
        SearchLocationScreenRobot(composeTestRule).apply {
            setIdleState()
            performSearch("London")
            verifySearchWasCalled("London")
        }
    }

    @Test
    fun searchLocationScreen_clearButton_clearsSearchAndClosesResults() {
        SearchLocationScreenRobot(composeTestRule).apply {
            setSuccessState()
            typeInSearchField("London")
            clickClearButton()
            verifySearchWasCalled("")
        }
    }

    @Test
    fun searchLocationScreen_locationSelection_triggersNavigation() {
        SearchLocationScreenRobot(composeTestRule).apply {
            setSuccessState()
            typeInSearchField("London")
            clickFirstSearchResult()
            verifyLocationSelectionWasCalled()
        }
    }

    @Test
    fun searchLocationScreen_searchPlaceholder_isDisplayed() {
        SearchLocationScreenRobot(composeTestRule).apply {
            setIdleState()
            assertSearchPlaceholderIsDisplayed()
        }
    }
}

class SearchLocationScreenRobot(
    private val composeTestRule: ComposeContentTestRule,
) {
    private fun createMockSuccessState(
        locations: List<SearchResult> = createMockSearchResults(),
        query: String = "London",
    ) = SearchLocationUiState.Success(locations = locations, query = query)

    private fun createMockSearchResults() =
        listOf(
            SearchResult(
                displayName = "London, UK",
                name = "London",
                latitude = 51.5074,
                longitude = -0.1278,
                country = "United Kingdom",
            ),
            SearchResult(
                displayName = "London, Ontario, Canada",
                name = "London",
                latitude = 42.9849,
                longitude = -81.2453,
                country = "Canada",
                state = "Ontario",
            ),
        )

    private val mockViewModel = mockk<SearchLocationViewModel>(relaxed = true)
    private val onNavigateToWeather: (Location) -> Unit = mockk(relaxed = true)

    private fun setupScreen(state: SearchLocationUiState): SearchLocationScreenRobot {
        every { mockViewModel.uiState } returns MutableStateFlow(state)

        composeTestRule.setContent {
            TempestTheme {
                SearchLocationScreen(
                    viewModel = mockViewModel,
                    onNavigateToWeather = onNavigateToWeather,
                )
            }
        }
        return this
    }

    fun setIdleState(): SearchLocationScreenRobot = setupScreen(SearchLocationUiState.Idle)

    fun setLoadingState(): SearchLocationScreenRobot = setupScreen(SearchLocationUiState.Loading)

    fun setSuccessState(): SearchLocationScreenRobot = setupScreen(createMockSuccessState())

    fun setSuccessStateWithEmptyResults(): SearchLocationScreenRobot =
        setupScreen(createMockSuccessState(locations = emptyList(), query = "xyz"))

    fun setErrorState(message: String): SearchLocationScreenRobot =
        setupScreen(SearchLocationUiState.Error(message = message, query = "test"))

    fun performSearch(query: String) {
        composeTestRule.onNodeWithText("Search for a location…").performTextInput(query)
    }

    fun typeInSearchField(query: String) {
        composeTestRule.onNodeWithText("Search for a location…").performTextInput(query)
    }

    fun clickClearButton() {
        composeTestRule.onNodeWithContentDescription("Clear").performClick()
    }

    fun clickFirstSearchResult() {
        composeTestRule.onNodeWithText("London, UK").performClick()
    }

    fun assertWelcomeMessageIsDisplayed() {
        composeTestRule.onNodeWithText("Find Location").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search for cities worldwide").assertIsDisplayed()
    }

    fun assertLoadingIndicatorIsDisplayed() {
        composeTestRule.onNodeWithText("Searching locations…").assertIsDisplayed()
    }

    fun assertSearchResultsAreDisplayed() {
        composeTestRule.onNodeWithText("London, UK").assertIsDisplayed()
        composeTestRule.onNodeWithText("London, Ontario, Canada").assertIsDisplayed()
    }

    fun assertEmptyStateIsDisplayed() {
        composeTestRule.onNodeWithText("No locations found").assertIsDisplayed()
    }

    fun assertEmptyStateIsNotDisplayed() {
        composeTestRule.onNodeWithText("No locations found").assertIsNotDisplayed()
    }

    fun assertSearchPlaceholderIsDisplayed() {
        composeTestRule.onNodeWithText("Search for a location…").assertIsDisplayed()
    }

    fun verifySearchWasCalled(query: String) {
        verify { mockViewModel.searchLocations(query) }
    }

    fun verifyLocationSelectionWasCalled() {
        verify { mockViewModel.onLocationSelected(any()) }
    }
}
