package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.CoachResponse
import kotlinx.coroutines.flow.Flow

interface CoachRepository {
    fun getCoaches(): Flow<List<CoachResponse>>
    suspend fun getById(id: Long): Result<CoachResponse?>
    suspend fun getByClub(clubId: Long): Result<List<CoachResponse>>
    suspend fun createCoach(coach: CoachResponse): Result<CoachResponse>
    suspend fun updateCoach(id: Long, coach: CoachResponse): Result<Unit>
    suspend fun deleteCoach(id: Long): Result<Unit>
}
