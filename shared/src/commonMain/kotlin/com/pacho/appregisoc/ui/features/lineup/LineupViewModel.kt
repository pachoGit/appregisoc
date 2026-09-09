package com.pacho.appregisoc.ui.features.lineup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.CoachResponse
import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse
import com.pacho.appregisoc.data.dto.PlayerResponse
import com.pacho.appregisoc.domain.usecase.CloseLineupUseCase
import com.pacho.appregisoc.domain.usecase.GetCoachesUseCase
import com.pacho.appregisoc.domain.usecase.GetLineupUseCase
import com.pacho.appregisoc.domain.usecase.GetPhysicalTrainersUseCase
import com.pacho.appregisoc.domain.usecase.GetPlayersUseCase
import com.pacho.appregisoc.domain.usecase.SaveLineupUseCase
import com.pacho.appregisoc.domain.validation.LineupValidator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LineupUiState {
    data object Loading : LineupUiState()
    data class Error(val message: String) : LineupUiState()
    data class Success(
        val lineupId: Long?,
        val isClosed: Boolean,
        val playerIds: Set<Long>,
        val coachId: Long?,
        val physicalTrainerId: Long?
    ) : LineupUiState()
}

class LineupViewModel(
    private val getLineupUseCase: GetLineupUseCase,
    private val saveLineupUseCase: SaveLineupUseCase,
    private val closeLineupUseCase: CloseLineupUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val getCoachesUseCase: GetCoachesUseCase,
    private val getPhysicalTrainersUseCase: GetPhysicalTrainersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LineupUiState>(LineupUiState.Loading)
    val uiState: StateFlow<LineupUiState> = _uiState.asStateFlow()

    private val _players = MutableStateFlow<List<PlayerResponse>>(emptyList())
    val players: StateFlow<List<PlayerResponse>> = _players.asStateFlow()

    private val _coaches = MutableStateFlow<List<CoachResponse>>(emptyList())
    val coaches: StateFlow<List<CoachResponse>> = _coaches.asStateFlow()

    private val _physicalTrainers = MutableStateFlow<List<PhysicalTrainerResponse>>(emptyList())
    val physicalTrainers: StateFlow<List<PhysicalTrainerResponse>> = _physicalTrainers.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    private val matchId = MutableStateFlow(0L)
    private val clubId = MutableStateFlow(1L)

    fun load(matchId: Long, clubId: Long = 1L) {
        this.matchId.value = matchId
        this.clubId.value = clubId
        viewModelScope.launch {
            _uiState.value = LineupUiState.Loading
            when (val result = getLineupUseCase(matchId, clubId)) {
                is Result.Error -> _uiState.value = LineupUiState.Error(result.message)
                is Result.Success -> {
                    val lineup = result.data
                    _uiState.value = LineupUiState.Success(
                        lineupId = lineup?.id,
                        isClosed = false,
                        playerIds = lineup?.playerIds ?: emptySet(),
                        coachId = lineup?.coach?.coachId,
                        physicalTrainerId = lineup?.physicalTrainer?.physicalTrainerId
                    )
                    loadAvailableMembers(clubId)
                }
            }
        }
    }

    private fun loadAvailableMembers(clubId: Long) {
        viewModelScope.launch {
            _players.value = getSuccessData(getPlayersUseCase(clubId))
            _coaches.value = getSuccessData(getCoachesUseCase(clubId))
            _physicalTrainers.value = getSuccessData(getPhysicalTrainersUseCase(clubId))
        }
    }

    private fun <T> getSuccessData(result: Result<List<T>>): List<T> {
        return (result as? Result.Success)?.data ?: emptyList()
    }

    fun togglePlayer(playerId: Long) {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return

        val updated = if (playerId in state.playerIds) {
            state.playerIds - playerId
        } else {
            if (state.playerIds.size >= LineupValidator.MAX_PLAYERS) {
                viewModelScope.launch {
                    _snackBarMessage.emit("Máximo ${LineupValidator.MAX_PLAYERS} jugadores por planilla")
                }
                return
            }
            state.playerIds + playerId
        }
        _uiState.value = state.copy(playerIds = updated)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCoach(coachId: Long?) {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return
        _uiState.value = state.copy(coachId = coachId)
    }

    fun selectPhysicalTrainer(trainerId: Long?) {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return
        _uiState.value = state.copy(physicalTrainerId = trainerId)
    }

    fun saveDraft() {
        persist(isClose = false)
    }

    fun closeLineup() {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return

        if (state.lineupId != null) {
            viewModelScope.launch {
                _isSaving.value = true
                when (val result = closeLineupUseCase(state.lineupId)) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _uiState.value = state.copy(isClosed = true)
                        _snackBarMessage.emit("Planilla cerrada definitivamente")
                    }
                }
                _isSaving.value = false
            }
        } else {
            persist(isClose = true)
        }
    }

    private fun persist(isClose: Boolean) {
        val state = currentSuccessState() ?: return
        if (state.isClosed) return

        viewModelScope.launch {
            _isSaving.value = true
            val result = saveLineupUseCase(
                id = state.lineupId,
                matchId = matchId.value,
                clubId = clubId.value,
                playerIds = state.playerIds.toList(),
                coachId = state.coachId,
                physicalTrainerId = state.physicalTrainerId
            )
            _isSaving.value = false
            when (result) {
                is Result.Error -> _snackBarMessage.emit(result.message)
                is Result.Success -> {
                    val saved = result.data
                    _uiState.value = state.copy(
                        lineupId = saved.id,
                        isClosed = isClose,
                        playerIds = saved.playerIds,
                        coachId = saved.coach?.coachId,
                        physicalTrainerId = saved.physicalTrainer?.physicalTrainerId
                    )
                    _snackBarMessage.emit(
                        if (isClose) "Planilla cerrada definitivamente" else "Planilla guardada como borrador"
                    )
                }
            }
        }
    }

    private fun currentSuccessState(): LineupUiState.Success? {
        return _uiState.value as? LineupUiState.Success
    }
}