package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class GetPlayersUseCase(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Flow<List<Player>> = repository.getPlayers()
}
