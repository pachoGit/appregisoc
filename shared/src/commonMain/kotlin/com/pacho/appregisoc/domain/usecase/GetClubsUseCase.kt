package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.data.dto.ClubResponse
import com.pacho.appregisoc.domain.repository.ClubRepository
import kotlinx.coroutines.flow.Flow

class GetClubsUseCase(
    private val repository: ClubRepository
) {
    operator fun invoke(): Flow<List<ClubResponse>> = repository.getClubs()
}
