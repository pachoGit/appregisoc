package com.pacho.appregisoc.data.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.datasource.PlayerDataSource
import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl(
    private val dataSource: PlayerDataSource
) : PlayerRepository {

    override fun getPlayers(): Flow<List<Player>> = dataSource.getPlayers()

    override suspend fun savePlayer(player: Player): Result<Unit> {
        return try {
            dataSource.savePlayer(player)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al guardar el jugador: ${e.message}", e)
        }
    }

    override suspend fun deletePlayer(id: String): Result<Unit> {
        return try {
            dataSource.deletePlayer(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al eliminar el jugador: ${e.message}", e)
        }
    }
}
