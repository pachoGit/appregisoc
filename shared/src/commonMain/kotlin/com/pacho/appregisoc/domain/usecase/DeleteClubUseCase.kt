package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.repository.ClubRepository

class DeleteClubUseCase(
    private val repository: ClubRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteClub(id)
}
