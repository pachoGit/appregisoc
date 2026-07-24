package com.pacho.appregisoc.ui.features.player

import com.pacho.appregisoc.domain.model.Player

data class PlayerFormState(
    val firstNames: String = "",
    val lastNames: String = "",
    val dni: String = "",
    val age: String = "",
    val birthDate: Long = 0L,
    val photoUrl: String = "",
    val dniFrontPhotoUrl: String = "",
    val dniBackPhotoUrl: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingPlayerId: String? = null
) {
    companion object {
        fun fromPlayer(player: Player) = PlayerFormState(
            firstNames = player.firstNames,
            lastNames = player.lastNames,
            dni = player.dni,
            age = player.age.toString(),
            birthDate = player.birthDate,
            photoUrl = player.photoUrl ?: "",
            dniFrontPhotoUrl = player.dniFrontPhotoUrl ?: "",
            dniBackPhotoUrl = player.dniBackPhotoUrl ?: "",
            isEditing = true,
            editingPlayerId = player.id
        )
    }
}
