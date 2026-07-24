package com.pacho.appregisoc.data.datasource

import com.pacho.appregisoc.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryPlayerDataSource : PlayerDataSource {

    private val playersFlow = MutableStateFlow<List<Player>>(emptyList())

    override fun getPlayers(): Flow<List<Player>> = playersFlow

    override suspend fun savePlayer(player: Player) {
        val currentList = playersFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == player.id }
        if (index != -1) {
            currentList[index] = player
        } else {
            currentList.add(player)
        }
        playersFlow.emit(currentList)
    }

    override suspend fun deletePlayer(id: String) {
        val currentList = playersFlow.value.filter { it.id != id }
        playersFlow.emit(currentList)
    }
}
