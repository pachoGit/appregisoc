package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.ClubResponse
import com.pacho.appregisoc.data.dto.CreateClubRequest
import com.pacho.appregisoc.data.dto.UpdateClubRequest
import kotlinx.coroutines.flow.Flow

interface ClubRepository {
    fun getClubs(): Flow<List<ClubResponse>>
    suspend fun createClub(request: CreateClubRequest): Result<ClubResponse>
    suspend fun updateClub(id: Long, request: UpdateClubRequest): Result<Unit>
    suspend fun deleteClub(id: Long): Result<Unit>
}
