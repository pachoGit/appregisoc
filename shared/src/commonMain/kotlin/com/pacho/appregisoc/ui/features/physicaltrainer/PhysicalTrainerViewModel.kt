package com.pacho.appregisoc.ui.features.physicaltrainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse
import com.pacho.appregisoc.domain.usecase.DeletePhysicalTrainerUseCase
import com.pacho.appregisoc.domain.usecase.GetPhysicalTrainersUseCase
import com.pacho.appregisoc.domain.usecase.SavePhysicalTrainerUseCase
import com.pacho.appregisoc.domain.usecase.UploadPhotoUseCase
import com.pacho.appregisoc.ui.components.PhotoPickerState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class PhysicalTrainerUiState {
    data object Loading : PhysicalTrainerUiState()
    data class Success(val trainers: List<PhysicalTrainerResponse>) : PhysicalTrainerUiState()
    data class Error(val message: String) : PhysicalTrainerUiState()
}

class PhysicalTrainerViewModel(
    private val getPhysicalTrainersUseCase: GetPhysicalTrainersUseCase,
    private val savePhysicalTrainerUseCase: SavePhysicalTrainerUseCase,
    private val deletePhysicalTrainerUseCase: DeletePhysicalTrainerUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhysicalTrainerUiState>(PhysicalTrainerUiState.Loading)
    val uiState: StateFlow<PhysicalTrainerUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    fun loadPhysicalTrainers(clubId: Long = 1L) {
        viewModelScope.launch {
            _uiState.value = PhysicalTrainerUiState.Loading
            when(val result = getPhysicalTrainersUseCase(clubId)) {
                is Result.Error -> _uiState.value = PhysicalTrainerUiState.Error(result.message)
                is Result.Success -> _uiState.value = PhysicalTrainerUiState.Success(result.data)
            }
        }
    }
    fun savePhysicalTrainer(formState: PhysicalTrainerFormState) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = savePhysicalTrainerUseCase(
                    id = formState.editingId,
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
                    is Result.Success -> _snackBarMessage.emit(
                        if (formState.isEditing) "Preparador físico actualizado correctamente"
                        else "Preparador físico registrado correctamente"
                    )
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun deletePhysicalTrainer(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deletePhysicalTrainerUseCase(id)
                when (result) {
                    is Result.Error -> _snackBarMessage.emit(result.message)
                    is Result.Success -> _snackBarMessage.emit("Preparador físico eliminado")
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
