package com.pacho.appregisoc.ui.navigation

import com.pacho.appregisoc.domain.model.Player

sealed class Screen {
    data object PlayerList : Screen()
    data object PlayerCreate : Screen()
    data class PlayerEdit(val player: Player) : Screen()
    data class PlayerDetail(val player: Player) : Screen()
}
