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
import com.pacho.appregisoc.ui.features.home.HomeScreen
import com.pacho.appregisoc.ui.features.player.*
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

    val playerUiState by playerViewModel.uiState.collectAsState()
    val clubUiState by clubViewModel.uiState.collectAsState()
    val playerIsLoading by playerViewModel.isLoading.collectAsState()
    val clubIsLoading by clubViewModel.isLoading.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        merge(playerViewModel.snackBarMessage, clubViewModel.snackBarMessage).collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    fun onTabSelected(tab: Int) {
        currentScreen = when (tab) {
            0 -> Screen.Home
            1 -> Screen.ClubList
            2 -> Screen.PlayerList
            else -> Screen.Home
        }
    }

    MaterialTheme {
        LoadingOverlay(isLoading = playerIsLoading || clubIsLoading) {
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
                                currentScreen = Screen.PlayerList
                            },
                            onCancel = { currentScreen = Screen.PlayerList },
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
                                currentScreen = Screen.PlayerList
                            },
                            onCancel = { currentScreen = Screen.PlayerList },
                            onUploadPhoto = { bytes, fileName, photoType, onStateUpdate ->
                                playerViewModel.uploadPhoto(bytes, fileName, photoType, onStateUpdate)
                            }
                        )
                    }
                    is Screen.PlayerDetail -> {
                        PlayerDetailScreen(
                            player = screen.player,
                            onBack = { currentScreen = Screen.PlayerList }
                        )
                    }
                }
            }
        }
    }
}
