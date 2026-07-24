package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun getPlayers(): Flow<List<Player>>
    suspend fun savePlayer(player: Player): Result<Unit>
    suspend fun deletePlayer(id: String): Result<Unit>
}
