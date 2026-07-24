package com.pacho.appregisoc.di

import com.pacho.appregisoc.data.MockPhotoUploadDataSource
import com.pacho.appregisoc.data.PhotoUploadDataSource
import com.pacho.appregisoc.data.PlayerApiService
import com.pacho.appregisoc.data.createHttpClient
import com.pacho.appregisoc.domain.repository.PlayerRepository
import com.pacho.appregisoc.domain.usecase.DeletePlayerUseCase
import com.pacho.appregisoc.domain.usecase.GetPlayersUseCase
import com.pacho.appregisoc.domain.usecase.SavePlayerUseCase
import com.pacho.appregisoc.domain.usecase.UploadPhotoUseCase

class AppModule {
    private val httpClient = createHttpClient()
    private val playerRepository: PlayerRepository = PlayerApiService(httpClient)
    private val photoUploadDataSource: PhotoUploadDataSource = MockPhotoUploadDataSource()

    val getPlayersUseCase = GetPlayersUseCase(playerRepository)
    val savePlayerUseCase = SavePlayerUseCase(playerRepository)
    val deletePlayerUseCase = DeletePlayerUseCase(playerRepository)
    val uploadPhotoUseCase = UploadPhotoUseCase(photoUploadDataSource)
}
