package com.harry.tempest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.location.domain.model.StartDestination
import com.harry.location.domain.usecase.GetStartDestinationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TempestViewModel(
    private val getStartDestinationUseCase: GetStartDestinationUseCase,
) : ViewModel() {
    private val _startDestination = MutableStateFlow<StartDestination?>(null)
    val startDestination: StateFlow<StartDestination?> = _startDestination.asStateFlow()

    init {
        determineStartDestination()
    }

    private fun determineStartDestination() {
        viewModelScope.launch {
            val destination = getStartDestinationUseCase()
            _startDestination.value = destination
        }
    }
}
