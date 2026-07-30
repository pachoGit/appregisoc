package com.pacho.appregisoc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pacho.appregisoc.di.AppModule
import com.pacho.appregisoc.ui.components.LoadingOverlay
import com.pacho.appregisoc.ui.features.club.*
import com.pacho.appregisoc.ui.features.coach.*
import com.pacho.appregisoc.ui.features.home.HomeScreen
import com.pacho.appregisoc.ui.features.physicaltrainer.*
import com.pacho.appregisoc.ui.features.player.*
import com.pacho.appregisoc.ui.features.staff.StaffScreen
import com.pacho.appregisoc.ui.layouts.MainLayout
import com.pacho.appregisoc.ui.navigation.Screen
import kotlinx.coroutines.flow.merge

@Composable
fun App() {
    val appModule = remember { AppModule() }

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
            deleteCoachUseCase = appModule.deleteCoachUseCase
        )
    }
    val physicalTrainerViewModel: PhysicalTrainerViewModel = viewModel {
        PhysicalTrainerViewModel(
            getPhysicalTrainersUseCase = appModule.getPhysicalTrainersUseCase,
            savePhysicalTrainerUseCase = appModule.savePhysicalTrainerUseCase,
            deletePhysicalTrainerUseCase = appModule.deletePhysicalTrainerUseCase
        )
    }

    val playerUiState by playerViewModel.uiState.collectAsState()
    val clubUiState by clubViewModel.uiState.collectAsState()
    val coachUiState by coachViewModel.uiState.collectAsState()
    val physicalTrainerUiState by physicalTrainerViewModel.uiState.collectAsState()

    val playerIsLoading by playerViewModel.isLoading.collectAsState()
    val clubIsLoading by clubViewModel.isLoading.collectAsState()
    val coachIsLoading by coachViewModel.isLoading.collectAsState()
    val physicalTrainerIsLoading by physicalTrainerViewModel.isLoading.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedStaffTab by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        merge(
            playerViewModel.snackBarMessage,
            clubViewModel.snackBarMessage,
            coachViewModel.snackBarMessage,
            physicalTrainerViewModel.snackBarMessage
        ).collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    fun onTabSelected(tab: Int) {
        currentScreen = when (tab) {
            0 -> Screen.Home
            1 -> Screen.ClubList
            2 -> Screen.Staff
            else -> Screen.Home
        }
    }

    MaterialTheme {
        val isLoading = playerIsLoading || clubIsLoading || coachIsLoading || physicalTrainerIsLoading
        LoadingOverlay(isLoading = isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (val screen = currentScreen) {
                    is Screen.Home -> {
                        MainLayout(
                            title = "Inicio",
                            selectedTab = 0,
                            onTabSelected = ::onTabSelected,
                            snackbarHost = { SnackbarHost(snackbarHostState) }
                        ) {
                            HomeScreen(
                                uiState = clubUiState,
                                onViewClub = { currentScreen = Screen.ClubDetail(it) }
                            )
                        }
                    }
                    is Screen.ClubList -> {
                        MainLayout(
                            title = "Clubs",
                            selectedTab = 1,
                            onTabSelected = ::onTabSelected,
                            snackbarHost = { SnackbarHost(snackbarHostState) }
                        ) {
                            ClubListScreen(
                                uiState = clubUiState,
                                onAddClub = { currentScreen = Screen.ClubCreate },
                                onEditClub = { currentScreen = Screen.ClubEdit(it) },
                                onDeleteClub = { clubViewModel.deleteClub(it) },
                                onViewClub = { currentScreen = Screen.ClubDetail(it) }
                            )
                        }
                    }
                    is Screen.ClubCreate -> {
                        ClubCreateScreen(
                            onSave = { formState ->
                                clubViewModel.createClub(formState)
                                currentScreen = Screen.ClubList
                            },
                            onCancel = { currentScreen = Screen.ClubList },
                            onUploadPhoto = { bytes, fileName, onStateUpdate ->
                                clubViewModel.uploadCrestPhoto(bytes, fileName, onStateUpdate)
                            }
                        )
                    }
                    is Screen.ClubEdit -> {
                        ClubEditScreen(
                            club = screen.club,
                            onSave = { formState ->
                                clubViewModel.updateClub(formState)
                                currentScreen = Screen.ClubList
                            },
                            onCancel = { currentScreen = Screen.ClubList },
                            onUploadPhoto = { bytes, fileName, onStateUpdate ->
                                clubViewModel.uploadCrestPhoto(bytes, fileName, onStateUpdate)
                            }
                        )
                    }
                    is Screen.ClubDetail -> {
                        ClubDetailScreen(
                            club = screen.club,
                            onBack = { currentScreen = Screen.ClubList }
                        )
                    }
                    is Screen.Staff -> {
                        MainLayout(
                            title = "Plantilla",
                            selectedTab = 2,
                            onTabSelected = ::onTabSelected,
                            snackbarHost = { SnackbarHost(snackbarHostState) }
                        ) {
                            StaffScreen(
                                selectedTab = selectedStaffTab,
                                onTabSelected = { selectedStaffTab = it },
                                playerUiState = playerUiState,
                                coachUiState = coachUiState,
                                physicalTrainerUiState = physicalTrainerUiState,
                                onAddPlayer = { currentScreen = Screen.PlayerCreate },
                                onEditPlayer = { currentScreen = Screen.PlayerEdit(it) },
                                onDeletePlayer = { playerViewModel.deletePlayer(it) },
                                onViewPlayer = { currentScreen = Screen.PlayerDetail(it) },
                                onAddCoach = { currentScreen = Screen.CoachCreate },
                                onEditCoach = { currentScreen = Screen.CoachEdit(it) },
                                onDeleteCoach = { coachViewModel.deleteCoach(it) },
                                onViewCoach = { currentScreen = Screen.CoachDetail(it) },
                                onAddTrainer = { currentScreen = Screen.PhysicalTrainerCreate },
                                onEditTrainer = { currentScreen = Screen.PhysicalTrainerEdit(it) },
                                onDeleteTrainer = { physicalTrainerViewModel.deletePhysicalTrainer(it) },
                                onViewTrainer = { currentScreen = Screen.PhysicalTrainerDetail(it) }
                            )
                        }
                    }
                    is Screen.PlayerList -> {
                        MainLayout(
                            title = "Jugadores",
                            selectedTab = 2,
                            onTabSelected = ::onTabSelected,
                            snackbarHost = { SnackbarHost(snackbarHostState) }
                        ) {
                            PlayerListScreen(
                                uiState = playerUiState,
                                onAddPlayer = { currentScreen = Screen.PlayerCreate },
                                onEditPlayer = { currentScreen = Screen.PlayerEdit(it) },
                                onDeletePlayer = { playerViewModel.deletePlayer(it) },
                                onViewPlayer = { currentScreen = Screen.PlayerDetail(it) }
                            )
                        }
                    }
                    is Screen.PlayerCreate -> {
                        PlayerCreateScreen(
                            onSave = { formState ->
                                playerViewModel.savePlayer(formState)
                                currentScreen = Screen.Staff
                            },
                            onCancel = { currentScreen = Screen.Staff },
                            onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                                playerViewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                            }
                        )
                    }
                    is Screen.PlayerEdit -> {
                        PlayerEditScreen(
                            player = screen.player,
                            onSave = { formState ->
                                playerViewModel.savePlayer(formState)
                                currentScreen = Screen.Staff
                            },
                            onCancel = { currentScreen = Screen.Staff },
                            onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                                playerViewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                            }
                        )
                    }
                    is Screen.PlayerDetail -> {
                        PlayerDetailScreen(
                            player = screen.player,
                            onBack = { currentScreen = Screen.Staff }
                        )
                    }
                    is Screen.CoachCreate -> {
                        CoachCreateScreen(
                            onSave = { formState ->
                                coachViewModel.saveCoach(formState)
                                currentScreen = Screen.Staff
                            },
                            onCancel = { currentScreen = Screen.Staff }
                        )
                    }
                    is Screen.CoachEdit -> {
                        CoachEditScreen(
                            coach = screen.coach,
                            onSave = { formState ->
                                coachViewModel.saveCoach(formState)
                                currentScreen = Screen.Staff
                            },
                            onCancel = { currentScreen = Screen.Staff }
                        )
                    }
                    is Screen.CoachDetail -> {
                        CoachDetailScreen(
                            coach = screen.coach,
                            onBack = { currentScreen = Screen.Staff }
                        )
                    }
                    is Screen.PhysicalTrainerCreate -> {
                        PhysicalTrainerCreateScreen(
                            onSave = { formState ->
                                physicalTrainerViewModel.savePhysicalTrainer(formState)
                                currentScreen = Screen.Staff
                            },
                            onCancel = { currentScreen = Screen.Staff }
                        )
                    }
                    is Screen.PhysicalTrainerEdit -> {
                        PhysicalTrainerEditScreen(
                            entity = screen.trainer,
                            onSave = { formState ->
                                physicalTrainerViewModel.savePhysicalTrainer(formState)
                                currentScreen = Screen.Staff
                            },
                            onCancel = { currentScreen = Screen.Staff }
                        )
                    }
                    is Screen.PhysicalTrainerDetail -> {
                        PhysicalTrainerDetailScreen(
                            entity = screen.trainer,
                            onBack = { currentScreen = Screen.Staff }
                        )
                    }
                }
            }
        }
    }
}
