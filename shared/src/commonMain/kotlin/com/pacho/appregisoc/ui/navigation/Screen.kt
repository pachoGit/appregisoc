package com.pacho.appregisoc.ui.navigation

import com.pacho.appregisoc.data.dto.PlayerResponse

sealed class Screen {
    data object PlayerList : Screen()
    data object PlayerCreate : Screen()
    data class PlayerEdit(val player: PlayerResponse) : Screen()
    data class PlayerDetail(val player: PlayerResponse) : Screen()
}
