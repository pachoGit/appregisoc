package com.pacho.appregisoc.ui.features.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.MatchDateResponse
import com.pacho.appregisoc.domain.usecase.GetMatchDatesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MatchDateUiState {
    data object Loading : MatchDateUiState()
    data class Success(val matchDates: List<MatchDateResponse>) : MatchDateUiState()
    data class Error(val message: String) : MatchDateUiState()
}

class MatchDateViewModel(
    private val getMatchDatesUseCase: GetMatchDatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MatchDateUiState>(MatchDateUiState.Loading)
    val uiState: StateFlow<MatchDateUiState> = _uiState.asStateFlow()

    fun loadMatchDates(eventId: Long) {
        viewModelScope.launch {
            _uiState.value = MatchDateUiState.Loading
            when (val result = getMatchDatesUseCase(eventId)) {
                is Result.Error -> _uiState.value = MatchDateUiState.Error(result.message)
                is Result.Success -> _uiState.value = MatchDateUiState.Success(result.data)
            }
        }
    }
}
