package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.repository.PlayerRepository

class DeletePlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deletePlayer(id)
}
