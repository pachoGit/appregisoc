package com.pacho.appregisoc.ui.features.home

import androidx.compose.runtime.Composable
import com.pacho.appregisoc.ui.features.club.ClubUiState
import com.pacho.appregisoc.ui.layouts.MainLayout
import com.pacho.appregisoc.ui.navigation.AppNavigator
import com.pacho.appregisoc.ui.navigation.Screen

@Composable
fun HomeRoute(
    uiState: ClubUiState,
    navigator: AppNavigator,
    snackbarHost: @Composable () -> Unit
) {
    MainLayout(
        title = "Inicio",
        selectedTab = 0,
        onTabSelected = navigator::navigateToTab,
        snackbarHost = snackbarHost
    ) {
        HomeScreen(
            uiState = uiState,
            onViewClub = { navigator.navigateTo(Screen.Club.Detail(it)) }
        )
    }
}