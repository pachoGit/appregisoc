package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.repository.LineupRepository

class CloseLineupUseCase(
    private val repository: LineupRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repository.close(id)
    }
}