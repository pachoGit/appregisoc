package com.pacho.appregisoc.di

import com.pacho.appregisoc.data.CoachApiService
import com.pacho.appregisoc.data.ClubApiService
import com.pacho.appregisoc.data.EventApiService
import com.pacho.appregisoc.data.MatchDateApiService
import com.pacho.appregisoc.data.PhotoUploadDataSource
import com.pacho.appregisoc.data.PhysicalTrainerApiService
import com.pacho.appregisoc.data.PlayerApiService
import com.pacho.appregisoc.data.apiBaseUrl
import com.pacho.appregisoc.data.createHttpClient
import com.pacho.appregisoc.data.mock.MockPhotoUploadDataSource
import com.pacho.appregisoc.domain.repository.CoachRepository
import com.pacho.appregisoc.domain.repository.ClubRepository
import com.pacho.appregisoc.domain.repository.EventRepository
import com.pacho.appregisoc.domain.repository.MatchDateRepository
import com.pacho.appregisoc.domain.repository.PhysicalTrainerRepository
import com.pacho.appregisoc.domain.repository.PlayerRepository
import com.pacho.appregisoc.domain.usecase.CreateClubUseCase
import com.pacho.appregisoc.domain.usecase.CreateEventUseCase
import com.pacho.appregisoc.domain.usecase.DeleteClubUseCase
import com.pacho.appregisoc.domain.usecase.DeleteCoachUseCase
import com.pacho.appregisoc.domain.usecase.DeleteEventUseCase
import com.pacho.appregisoc.domain.usecase.DeletePhysicalTrainerUseCase
import com.pacho.appregisoc.domain.usecase.DeletePlayerUseCase
import com.pacho.appregisoc.domain.usecase.GetClubsUseCase
import com.pacho.appregisoc.domain.usecase.GetCoachesUseCase
import com.pacho.appregisoc.domain.usecase.GetEventsUseCase
import com.pacho.appregisoc.domain.usecase.GetMatchDatesUseCase
import com.pacho.appregisoc.domain.usecase.GetPhysicalTrainersUseCase
import com.pacho.appregisoc.domain.usecase.GetPlayersUseCase
import com.pacho.appregisoc.domain.usecase.SaveCoachUseCase
import com.pacho.appregisoc.domain.usecase.SavePhysicalTrainerUseCase
import com.pacho.appregisoc.domain.usecase.SavePlayerUseCase
import com.pacho.appregisoc.domain.usecase.UpdateClubUseCase
import com.pacho.appregisoc.domain.usecase.UpdateEventUseCase
import com.pacho.appregisoc.domain.usecase.UploadPhotoUseCase

class AppModule {
    private val httpClient = createHttpClient()
    private val playerRepository: PlayerRepository = PlayerApiService(httpClient, "$apiBaseUrl/players")

    private val clubRepository: ClubRepository = ClubApiService(httpClient, "$apiBaseUrl/clubs")

    private val coachRepository: CoachRepository = CoachApiService(httpClient, "$apiBaseUrl/coaches")

    private val physicalTrainerRepository: PhysicalTrainerRepository = PhysicalTrainerApiService(httpClient, "$apiBaseUrl/physical-trainers")

    private val eventRepository: EventRepository = EventApiService(httpClient, "$apiBaseUrl/events")

    private val matchDateRepository: MatchDateRepository = MatchDateApiService(httpClient, "$apiBaseUrl/match-dates")

    private val photoUploadDataSource: PhotoUploadDataSource = MockPhotoUploadDataSource()

    val getPlayersUseCase = GetPlayersUseCase(playerRepository)
    val savePlayerUseCase = SavePlayerUseCase(playerRepository)
    val deletePlayerUseCase = DeletePlayerUseCase(playerRepository)
    val uploadPhotoUseCase = UploadPhotoUseCase(photoUploadDataSource)

    val getCoachesUseCase = GetCoachesUseCase(coachRepository)
    val saveCoachUseCase = SaveCoachUseCase(coachRepository)
    val deleteCoachUseCase = DeleteCoachUseCase(coachRepository)

    val getPhysicalTrainersUseCase = GetPhysicalTrainersUseCase(physicalTrainerRepository)
    val savePhysicalTrainerUseCase = SavePhysicalTrainerUseCase(physicalTrainerRepository)
    val deletePhysicalTrainerUseCase = DeletePhysicalTrainerUseCase(physicalTrainerRepository)

    val getClubsUseCase = GetClubsUseCase(clubRepository)
    val createClubUseCase = CreateClubUseCase(clubRepository)
    val updateClubUseCase = UpdateClubUseCase(clubRepository)
    val deleteClubUseCase = DeleteClubUseCase(clubRepository)

    val getEventsUseCase = GetEventsUseCase(eventRepository)
    val createEventUseCase = CreateEventUseCase(eventRepository)
    val updateEventUseCase = UpdateEventUseCase(eventRepository)
    val deleteEventUseCase = DeleteEventUseCase(eventRepository)

    val getMatchDatesUseCase = GetMatchDatesUseCase(matchDateRepository)
}
