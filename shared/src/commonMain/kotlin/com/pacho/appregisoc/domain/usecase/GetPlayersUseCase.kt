package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.data.dto.PlayerResponse
import com.pacho.appregisoc.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class GetPlayersUseCase(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Flow<List<PlayerResponse>> = repository.getPlayers()
}
