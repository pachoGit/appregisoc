package com.pacho.appregisoc.data.mock

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.auth.ClubInfoResponse
import com.pacho.appregisoc.data.dto.auth.LoginRequest
import com.pacho.appregisoc.data.dto.auth.LoginResponse
import com.pacho.appregisoc.data.dto.auth.MeResponse
import com.pacho.appregisoc.domain.repository.AuthRepository

class MockAuthRepository : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return Result.Success(
            LoginResponse(
                token = "mock-token-12345",
                userId = 1L,
                username = request.username,
                role = "CLUB_MANAGER",
                clubId = 1L
            )
        )
    }

    override suspend fun me(): Result<MeResponse> {
        return Result.Success(
            MeResponse(
                id = 1L,
                name = "Usuario",
                surname = "Mock",
                documentNumber = "12345678",
                username = "admin",
                role = "CLUB_MANAGER",
                club = ClubInfoResponse(id = 1L, name = "Club Mock")
            )
        )
    }
}
