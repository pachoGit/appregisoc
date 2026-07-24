package com.pacho.appregisoc.di

import com.pacho.appregisoc.data.datasource.InMemoryPlayerDataSource
import com.pacho.appregisoc.data.datasource.PlayerDataSource
import com.pacho.appregisoc.data.repository.PlayerRepositoryImpl
import com.pacho.appregisoc.domain.repository.PlayerRepository
import com.pacho.appregisoc.domain.usecase.DeletePlayerUseCase
import com.pacho.appregisoc.domain.usecase.GetPlayersUseCase
import com.pacho.appregisoc.domain.usecase.SavePlayerUseCase

class AppModule {
    private val playerDataSource: PlayerDataSource = InMemoryPlayerDataSource()
    private val playerRepository: PlayerRepository = PlayerRepositoryImpl(playerDataSource)

    val getPlayersUseCase = GetPlayersUseCase(playerRepository)
    val savePlayerUseCase = SavePlayerUseCase(playerRepository)
    val deletePlayerUseCase = DeletePlayerUseCase(playerRepository)
}
