package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.repository.CoachRepository

class DeleteCoachUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteCoach(id)
}
