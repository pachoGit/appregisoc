package com.pacho.appregisoc.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AppNavigator {
    var currentScreen by mutableStateOf<Screen>(Screen.Home)
        private set

    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }

    fun navigateToTab(tab: Int) {
        currentScreen = when (tab) {
            0 -> Screen.Home
            1 -> Screen.Event.List
            2 -> Screen.Staff.Overview
            else -> Screen.Home
        }
    }
}