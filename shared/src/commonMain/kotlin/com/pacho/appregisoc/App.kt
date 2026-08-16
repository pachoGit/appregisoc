package com.pacho.appregisoc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pacho.appregisoc.di.AppModule
import com.pacho.appregisoc.ui.components.LoadingOverlay
import com.pacho.appregisoc.ui.features.club.ClubRoute
import com.pacho.appregisoc.ui.features.club.ClubViewModel
import com.pacho.appregisoc.ui.features.coach.CoachRoute
import com.pacho.appregisoc.ui.features.coach.CoachViewModel
import com.pacho.appregisoc.ui.features.event.EventRoute
import com.pacho.appregisoc.ui.features.event.EventViewModel
import com.pacho.appregisoc.ui.features.event.MatchDateViewModel
import com.pacho.appregisoc.ui.features.home.HomeRoute
import com.pacho.appregisoc.ui.features.physicaltrainer.PhysicalTrainerRoute
import com.pacho.appregisoc.ui.features.physicaltrainer.PhysicalTrainerViewModel
import com.pacho.appregisoc.ui.features.player.PlayerRoute
import com.pacho.appregisoc.ui.features.player.PlayerViewModel
import com.pacho.appregisoc.ui.features.staff.StaffRoute
import com.pacho.appregisoc.ui.navigation.AppNavigator
import com.pacho.appregisoc.ui.navigation.Screen
import kotlinx.coroutines.flow.merge

@Composable
fun App() {
    val appModule = remember { AppModule() }
    val navigator = remember { AppNavigator() }
    val snackbarHostState = remember { SnackbarHostState() }

    val playerViewModel: PlayerViewModel = viewModel {
        PlayerViewModel(
            getPlayersUseCase = appModule.getPlayersUseCase,
            savePlayerUseCase = appModule.savePlayerUseCase,
            deletePlayerUseCase = appModule.deletePlayerUseCase,
            uploadPhotoUseCase = appModule.uploadPhotoUseCase
        )
    }
    val clubViewModel: ClubViewModel = viewModel {
        ClubViewModel(
            getClubsUseCase = appModule.getClubsUseCase,
            createClubUseCase = appModule.createClubUseCase,
            updateClubUseCase = appModule.updateClubUseCase,
            deleteClubUseCase = appModule.deleteClubUseCase,
            uploadPhotoUseCase = appModule.uploadPhotoUseCase
        )
    }
    val coachViewModel: CoachViewModel = viewModel {
        CoachViewModel(
            getCoachesUseCase = appModule.getCoachesUseCase,
            saveCoachUseCase = appModule.saveCoachUseCase,
            deleteCoachUseCase = appModule.deleteCoachUseCase,
            uploadPhotoUseCase = appModule.uploadPhotoUseCase
        )
    }
    val physicalTrainerViewModel: PhysicalTrainerViewModel = viewModel {
        PhysicalTrainerViewModel(
            getPhysicalTrainersUseCase = appModule.getPhysicalTrainersUseCase,
            savePhysicalTrainerUseCase = appModule.savePhysicalTrainerUseCase,
            deletePhysicalTrainerUseCase = appModule.deletePhysicalTrainerUseCase,
            uploadPhotoUseCase = appModule.uploadPhotoUseCase
        )
    }
    val eventViewModel: EventViewModel = viewModel {
        EventViewModel(
            getEventsUseCase = appModule.getEventsUseCase,
            createEventUseCase = appModule.createEventUseCase,
            updateEventUseCase = appModule.updateEventUseCase,
            deleteEventUseCase = appModule.deleteEventUseCase
        )
    }
    val matchDateViewModel: MatchDateViewModel = viewModel {
        MatchDateViewModel(
            getMatchDatesUseCase = appModule.getMatchDatesUseCase
        )
    }

    val playerUiState by playerViewModel.uiState.collectAsState()
    val clubUiState by clubViewModel.uiState.collectAsState()
    val coachUiState by coachViewModel.uiState.collectAsState()
    val physicalTrainerUiState by physicalTrainerViewModel.uiState.collectAsState()
    val eventUiState by eventViewModel.uiState.collectAsState()
    val matchDateUiState by matchDateViewModel.uiState.collectAsState()

    val playerIsLoading by playerViewModel.isLoading.collectAsState()
    val clubIsLoading by clubViewModel.isLoading.collectAsState()
    val coachIsLoading by coachViewModel.isLoading.collectAsState()
    val physicalTrainerIsLoading by physicalTrainerViewModel.isLoading.collectAsState()
    val eventIsLoading by eventViewModel.isLoading.collectAsState()

    var selectedStaffTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        merge(
            playerViewModel.snackBarMessage,
            clubViewModel.snackBarMessage,
            coachViewModel.snackBarMessage,
            physicalTrainerViewModel.snackBarMessage,
            eventViewModel.snackBarMessage
        ).collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    MaterialTheme {
        val isLoading = playerIsLoading || clubIsLoading || coachIsLoading || physicalTrainerIsLoading || eventIsLoading
        LoadingOverlay(isLoading = isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                val snackbarHost: @Composable () -> Unit = { SnackbarHost(snackbarHostState) }
                when (val screen = navigator.currentScreen) {
                    is Screen.Home -> HomeRoute(
                        uiState = clubUiState,
                        navigator = navigator,
                        snackbarHost = snackbarHost
                    )
                    is Screen.Club -> ClubRoute(
                        screen = screen,
                        uiState = clubUiState,
                        viewModel = clubViewModel,
                        navigator = navigator,
                        snackbarHost = snackbarHost
                    )
                    is Screen.Event -> EventRoute(
                        screen = screen,
                        uiState = eventUiState,
                        matchDateUiState = matchDateUiState,
                        viewModel = eventViewModel,
                        matchDateViewModel = matchDateViewModel,
                        navigator = navigator,
                        snackbarHost = snackbarHost
                    )
                    is Screen.Staff -> StaffRoute(
                        uiState = playerUiState,
                        coachUiState = coachUiState,
                        physicalTrainerUiState = physicalTrainerUiState,
                        viewModel = playerViewModel,
                        coachViewModel = coachViewModel,
                        physicalTrainerViewModel = physicalTrainerViewModel,
                        navigator = navigator,
                        selectedTab = selectedStaffTab,
                        onSelectedTabChange = { selectedStaffTab = it },
                        snackbarHost = snackbarHost
                    )
                    is Screen.Player -> PlayerRoute(
                        screen = screen,
                        viewModel = playerViewModel,
                        navigator = navigator
                    )
                    is Screen.Coach -> CoachRoute(
                        screen = screen,
                        viewModel = coachViewModel,
                        navigator = navigator
                    )
                    is Screen.PhysicalTrainer -> PhysicalTrainerRoute(
                        screen = screen,
                        viewModel = physicalTrainerViewModel,
                        navigator = navigator
                    )
                }
            }
        }
    }
}