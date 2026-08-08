package com.pacho.appregisoc.ui.features.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.CoachResponse
import com.pacho.appregisoc.domain.usecase.DeleteCoachUseCase
import com.pacho.appregisoc.domain.usecase.GetCoachesUseCase
import com.pacho.appregisoc.domain.usecase.SaveCoachUseCase
import com.pacho.appregisoc.domain.usecase.UploadPhotoUseCase
import com.pacho.appregisoc.ui.components.PhotoPickerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class CoachUiState {
    data object Loading : CoachUiState()
    data class Success(val coaches: List<CoachResponse>) : CoachUiState()
    data class Error(val message: String) : CoachUiState()
}

class CoachViewModel(
    private val getCoachesUseCase: GetCoachesUseCase,
    private val saveCoachUseCase: SaveCoachUseCase,
    private val deleteCoachUseCase: DeleteCoachUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CoachUiState>(CoachUiState.Loading)
    val uiState: StateFlow<CoachUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    fun loadCoaches(clubId: Long = 1L) {
        viewModelScope.launch {
            _uiState.value = CoachUiState.Loading
            when (val result = getCoachesUseCase(clubId)) {
                is Result.Error -> _uiState.value = CoachUiState.Error(result.message)
                is Result.Success -> _uiState.value = CoachUiState.Success(result.data)
            }
        }
    }

    fun saveCoach(formState: CoachFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = saveCoachUseCase(
                    id = formState.editingCoachId,
                    firstName = formState.firstName,
                    lastName = formState.lastName,
                    documentNumber = formState.documentNumber,
                    age = formState.age,
                    dateOfBirth = formState.dateOfBirth,
                    clubId = formState.clubId,
                    photoUrl = formState.photoUrl.ifBlank { null },
                    documentFrontUrl = formState.documentFrontUrl.ifBlank { null },
                    documentBackUrl = formState.documentBackUrl.ifBlank { null }
                )
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit(
                            if (formState.isEditing) "Entrenador actualizado correctamente"
                            else "Entrenador registrado correctamente"
                        )
                        loadCoaches(formState.clubId)
                    }
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCoach(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deleteCoachUseCase(id)
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> {
                        _snackBarMessage.emit("Entrenador eliminado")
                        loadCoaches()
                    }
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
