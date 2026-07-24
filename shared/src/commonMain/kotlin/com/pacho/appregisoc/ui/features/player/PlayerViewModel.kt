package com.pacho.appregisoc.ui.features.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.domain.usecase.DeletePlayerUseCase
import com.pacho.appregisoc.domain.usecase.GetPlayersUseCase
import com.pacho.appregisoc.domain.usecase.SavePlayerUseCase
import com.pacho.appregisoc.domain.usecase.UploadPhotoUseCase
import com.pacho.appregisoc.ui.components.PhotoPickerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class PlayerUiState {
    data object Loading : PlayerUiState()
    data class Success(val players: List<Player>) : PlayerUiState()
    data class Error(val message: String) : PlayerUiState()
}

class PlayerViewModel(
    private val getPlayersUseCase: GetPlayersUseCase,
    private val savePlayerUseCase: SavePlayerUseCase,
    private val deletePlayerUseCase: DeletePlayerUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    init {
        loadPlayers()
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            getPlayersUseCase()
                .onStart { _uiState.value = PlayerUiState.Loading }
                .catch { e -> _uiState.value = PlayerUiState.Error(e.message ?: "Error desconocido") }
                .collect { list ->
                    _uiState.value = PlayerUiState.Success(list)
                }
        }
    }

    fun savePlayer(formState: PlayerFormState) {
        viewModelScope.launch {
            val result = savePlayerUseCase(
                id = formState.editingPlayerId,
                firstNames = formState.firstNames,
                lastNames = formState.lastNames,
                dni = formState.dni,
                age = formState.age,
                birthDate = formState.birthDate,
                photoUrl = formState.photoUrl.ifBlank { null },
                dniFrontPhotoUrl = formState.dniFrontPhotoUrl.ifBlank { null },
                dniBackPhotoUrl = formState.dniBackPhotoUrl.ifBlank { null }
            )
            when (result) {
                is Result.Error -> _snackBarMessage.emit(result.message)
                is Result.Success -> _snackBarMessage.emit(
                    if (formState.isEditing) "Jugador actualizado correctamente"
                    else "Jugador registrado correctamente"
                )
            }
        }
    }

    fun deletePlayer(id: String) {
        viewModelScope.launch {
            val result = deletePlayerUseCase(id)
            when (result) {
                is Result.Error -> _snackBarMessage.emit(result.message)
                is Result.Success -> _snackBarMessage.emit("Jugador eliminado")
            }
        }
    }

    fun uploadPhoto(
        imageBytes: ByteArray,
        fileName: String,
        photoType: PhotoType,
        onStateUpdate: (PhotoPickerState) -> Unit
    ) {
        viewModelScope.launch {
            onStateUpdate(PhotoPickerState(isUploading = true))

            val result = uploadPhotoUseCase(imageBytes, fileName)
            when (result) {
                is Result.Error -> {
                    onStateUpdate(PhotoPickerState(error = result.message))
                    _snackBarMessage.emit(result.message)
                }
                is Result.Success -> {
                    onStateUpdate(PhotoPickerState(remoteUrl = result.data))
                    _snackBarMessage.emit("Foto subida correctamente")
                }
            }
        }
    }
}
