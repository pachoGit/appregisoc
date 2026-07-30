package com.pacho.appregisoc.di

import com.pacho.appregisoc.data.ClubApiService
import com.pacho.appregisoc.data.MockPhotoUploadDataSource
import com.pacho.appregisoc.data.PhotoUploadDataSource
import com.pacho.appregisoc.data.PlayerApiService
import com.pacho.appregisoc.data.apiBaseUrl
import com.pacho.appregisoc.data.createHttpClient
import com.pacho.appregisoc.domain.repository.ClubRepository
import com.pacho.appregisoc.domain.repository.PlayerRepository
import com.pacho.appregisoc.domain.usecase.CreateClubUseCase
import com.pacho.appregisoc.domain.usecase.DeleteClubUseCase
import com.pacho.appregisoc.domain.usecase.DeletePlayerUseCase
import com.pacho.appregisoc.domain.usecase.GetClubsUseCase
import com.pacho.appregisoc.domain.usecase.GetPlayersUseCase
import com.pacho.appregisoc.domain.usecase.SavePlayerUseCase
import com.pacho.appregisoc.domain.usecase.UpdateClubUseCase
import com.pacho.appregisoc.domain.usecase.UploadPhotoUseCase

class AppModule {
    private val httpClient = createHttpClient()
    private val playerRepository: PlayerRepository = PlayerApiService(httpClient, apiBaseUrl)

    private val clubApiBaseUrl = apiBaseUrl.replace("/players", "/clubs")
    private val clubRepository: ClubRepository = ClubApiService(httpClient, clubApiBaseUrl)

    private val photoUploadDataSource: PhotoUploadDataSource = MockPhotoUploadDataSource()

    val getPlayersUseCase = GetPlayersUseCase(playerRepository)
    val savePlayerUseCase = SavePlayerUseCase(playerRepository)
    val deletePlayerUseCase = DeletePlayerUseCase(playerRepository)
    val uploadPhotoUseCase = UploadPhotoUseCase(photoUploadDataSource)

    val getClubsUseCase = GetClubsUseCase(clubRepository)
    val createClubUseCase = CreateClubUseCase(clubRepository)
    val updateClubUseCase = UpdateClubUseCase(clubRepository)
    val deleteClubUseCase = DeleteClubUseCase(clubRepository)
}
