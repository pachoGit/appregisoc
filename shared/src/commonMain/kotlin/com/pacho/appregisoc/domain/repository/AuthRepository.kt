package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.auth.LoginRequest
import com.pacho.appregisoc.data.dto.auth.LoginResponse
import com.pacho.appregisoc.data.dto.auth.MeResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>

    suspend fun me(): Result<MeResponse>
}