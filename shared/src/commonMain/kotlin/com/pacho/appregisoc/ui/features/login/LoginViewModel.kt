package com.pacho.appregisoc.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

data class LoginFormState(
    val username: String = "",
    val password: String = "",
    val isMockMode: Boolean = false
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()

    private val _snackBarMessage = MutableSharedFlow<String>()
    val snackBarMessage: SharedFlow<String> = _snackBarMessage.asSharedFlow()

    fun onUsernameChange(username: String) {
        _formState.update { it.copy(username = username) }
    }

    fun onPasswordChange(password: String) {
        _formState.update { it.copy(password = password) }
    }

    fun onMockModeToggle(enabled: Boolean) {
        _formState.update { it.copy(isMockMode = enabled) }
    }

    fun onMockLoginSuccess() {
        _uiState.value = LoginUiState.Success
    }

    fun login() {
        val state = _formState.value

        if (state.username.isBlank()) {
            _uiState.value = LoginUiState.Error("El usuario es obligatorio")
            return
        }
        if (state.password.isBlank()) {
            _uiState.value = LoginUiState.Error("La contraseña es obligatoria")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val result = loginUseCase(
                username = state.username,
                password = state.password
            )
            when (result) {
                is Result.Success -> _uiState.value = LoginUiState.Success
                is Result.Error -> {
                    _uiState.value = LoginUiState.Error(result.message)
                    _snackBarMessage.emit(result.message)
                }
            }
        }
    }
}
