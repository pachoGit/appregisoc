package com.pacho.appregisoc.ui.navigation

import com.pacho.appregisoc.data.dto.ClubResponse
import com.pacho.appregisoc.data.dto.PlayerResponse

sealed class Screen {
    data object Home : Screen()
    data object ClubList : Screen()
    data object ClubCreate : Screen()
    data class ClubEdit(val club: ClubResponse) : Screen()
    data class ClubDetail(val club: ClubResponse) : Screen()

    data object PlayerList : Screen()
    data object PlayerCreate : Screen()
    data class PlayerEdit(val player: PlayerResponse) : Screen()
    data class PlayerDetail(val player: PlayerResponse) : Screen()
}
