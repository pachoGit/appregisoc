package com.pacho.appregisoc.data.datasource

import com.pacho.appregisoc.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerDataSource {
    fun getPlayers(): Flow<List<Player>>
    suspend fun savePlayer(player: Player)
    suspend fun deletePlayer(id: String)
}
