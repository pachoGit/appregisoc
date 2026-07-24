package com.pacho.appregisoc.ui.features.player

import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.ui.components.PhotoPickerState

data class PlayerFormState(
    val firstNames: String = "",
    val lastNames: String = "",
    val dni: String = "",
    val age: String = "",
    val birthDate: Long = 0L,
    val photoState: PhotoPickerState = PhotoPickerState(),
    val dniFrontPhotoState: PhotoPickerState = PhotoPickerState(),
    val dniBackPhotoState: PhotoPickerState = PhotoPickerState(),
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingPlayerId: String? = null
) {
    val photoUrl: String get() = photoState.remoteUrl ?: ""
    val dniFrontPhotoUrl: String get() = dniFrontPhotoState.remoteUrl ?: ""
    val dniBackPhotoUrl: String get() = dniBackPhotoState.remoteUrl ?: ""

    companion object {
        fun fromPlayer(player: Player) = PlayerFormState(
            firstNames = player.firstNames,
            lastNames = player.lastNames,
            dni = player.dni,
            age = player.age.toString(),
            birthDate = player.birthDate,
            photoState = PhotoPickerState(remoteUrl = player.photoUrl),
            dniFrontPhotoState = PhotoPickerState(remoteUrl = player.dniFrontPhotoUrl),
            dniBackPhotoState = PhotoPickerState(remoteUrl = player.dniBackPhotoUrl),
            isEditing = true,
            editingPlayerId = player.id
        )
    }
}
