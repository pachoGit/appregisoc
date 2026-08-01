package com.pacho.appregisoc.ui.features.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.PlayerResponse
import com.pacho.appregisoc.domain.usecase.DeletePlayerUseCase
import com.pacho.appregisoc.domain.usecase.GetPlayersUseCase
import com.pacho.appregisoc.domain.usecase.SavePlayerUseCase
import com.pacho.appregisoc.domain.usecase.UploadPhotoUseCase
import com.pacho.appregisoc.ui.components.PhotoPickerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class PlayerUiState {
    data object Loading : PlayerUiState()
    data class Success(val players: List<PlayerResponse>) : PlayerUiState()
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    fun loadPlayers(clubId: Long = 1L) {
        viewModelScope.launch {
            _uiState.value = PlayerUiState.Loading
            when (val result = getPlayersUseCase(clubId)) {
                is Result.Error -> _uiState.value = PlayerUiState.Error(result.message)
                is Result.Success -> _uiState.value = PlayerUiState.Success(result.data)
            }
        }
    }

    fun savePlayer(formState: PlayerFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = savePlayerUseCase(
                    id = formState.editingPlayerId,
                    firstName = formState.firstName,
                    lastName = formState.lastName,
                    documentNumber = formState.documentNumber,
                    age = formState.age,
                    dateOfBirth = formState.dateOfBirth,
                    clubId = formState.clubId,
                    position = formState.position,
                    photoUrl = formState.photoUrl.ifBlank { null },
                    documentFrontUrl = formState.documentFrontUrl.ifBlank { null },
                    documentBackUrl = formState.documentBackUrl.ifBlank { null }
                )
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> _snackBarMessage.emit(
                        if (formState.isEditing) "Jugador actualizado correctamente"
                        else "Jugador registrado correctamente"
                    )
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePlayer(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deletePlayerUseCase(id)
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> _snackBarMessage.emit("Jugador eliminado")
                }
            } finally {
                _isLoading.value = false
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
