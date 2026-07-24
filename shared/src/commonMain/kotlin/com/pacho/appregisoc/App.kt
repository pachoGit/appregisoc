package com.pacho.appregisoc

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pacho.appregisoc.di.AppModule
import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.ui.features.player.*
import com.pacho.appregisoc.ui.layouts.MainLayout
import com.pacho.appregisoc.ui.navigation.Screen

@Composable
fun App() {
    val appModule = remember { AppModule() }
    val viewModel: PlayerViewModel = viewModel {
        PlayerViewModel(
            getPlayersUseCase = appModule.getPlayersUseCase,
            savePlayerUseCase = appModule.savePlayerUseCase,
            deletePlayerUseCase = appModule.deletePlayerUseCase
        )
    }
    val uiState by viewModel.uiState.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.PlayerList) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackBarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            when (val screen = currentScreen) {
                is Screen.PlayerList -> {
                    MainLayout(
                        title = "Jugadores",
                        selectedTab = 2,
                        onTabSelected = { },
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) {
                        PlayerListScreen(
                            uiState = uiState,
                            onAddPlayer = { currentScreen = Screen.PlayerForm },
                            onEditPlayer = { currentScreen = Screen.PlayerEdit(it) },
                            onDeletePlayer = { viewModel.deletePlayer(it) },
                            onViewPlayer = { currentScreen = Screen.PlayerDetail(it) }
                        )
                    }
                }
                is Screen.PlayerForm -> {
                    PlayerFormScreen(
                        onSave = { formState ->
                            viewModel.savePlayer(formState)
                            currentScreen = Screen.PlayerList
                        },
                        onCancel = { currentScreen = Screen.PlayerList }
                    )
                }
                is Screen.PlayerEdit -> {
                    PlayerFormScreen(
                        player = (currentScreen as Screen.PlayerEdit).player,
                        onSave = { formState ->
                            viewModel.savePlayer(formState)
                            currentScreen = Screen.PlayerList
                        },
                        onCancel = { currentScreen = Screen.PlayerList }
                    )
                }
                is Screen.PlayerDetail -> {
                    PlayerDetailScreen(
                        player = (currentScreen as Screen.PlayerDetail).player,
                        onBack = { currentScreen = Screen.PlayerList }
                    )
                }
            }
        }
    }
}
