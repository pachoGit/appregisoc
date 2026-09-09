package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.auth.LoginRequest
import com.pacho.appregisoc.data.dto.auth.LoginResponse
import com.pacho.appregisoc.data.session.SessionManager
import com.pacho.appregisoc.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(username: String, password: String): Result<LoginResponse> {
        val request = LoginRequest(username = username, password = password)
        val result = authRepository.login(request)

        if (result is Result.Success) {
            sessionManager.saveLoginResponse(result.data)
            val meResult = authRepository.me()
            if (meResult is Result.Success) {
                sessionManager.saveMeResponse(meResult.data)
            }
        }

        return result
    }
}
