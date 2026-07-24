package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.PlayerResponse
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun getPlayers(): Flow<List<PlayerResponse>>
    suspend fun getById(id: Long): Result<PlayerResponse?>
    suspend fun getByClub(clubId: Long): Result<List<PlayerResponse>>
    suspend fun createPlayer(player: PlayerResponse): Result<PlayerResponse>
    suspend fun updatePlayer(id: Long, player: PlayerResponse): Result<Unit>
    suspend fun deletePlayer(id: Long): Result<Unit>
}
