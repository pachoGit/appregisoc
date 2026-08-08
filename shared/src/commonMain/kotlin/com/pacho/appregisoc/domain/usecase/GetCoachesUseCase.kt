package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.CoachResponse
import com.pacho.appregisoc.domain.repository.CoachRepository
import kotlinx.coroutines.flow.Flow

class GetCoachesUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(clubId: Long): Result<List<CoachResponse>> = repository.getByClub(clubId)
}
