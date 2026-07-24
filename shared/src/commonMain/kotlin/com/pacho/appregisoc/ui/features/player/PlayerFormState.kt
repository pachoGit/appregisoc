package com.pacho.appregisoc.ui.features.player

import com.pacho.appregisoc.data.dto.PlayerPosition
import com.pacho.appregisoc.data.dto.PlayerResponse
import com.pacho.appregisoc.ui.components.PhotoPickerState

data class PlayerFormState(
    val firstName: String = "",
    val lastName: String = "",
    val documentNumber: String = "",
    val age: String = "",
    val dateOfBirth: String = "",
    val photoState: PhotoPickerState = PhotoPickerState(),
    val dniFrontPhotoState: PhotoPickerState = PhotoPickerState(),
    val dniBackPhotoState: PhotoPickerState = PhotoPickerState(),
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingPlayerId: Long? = null,
    val clubId: Long = 1L,
    val position: PlayerPosition? = null
) {
    val photoUrl: String get() = photoState.remoteUrl ?: ""
    val documentFrontUrl: String get() = dniFrontPhotoState.remoteUrl ?: ""
    val documentBackUrl: String get() = dniBackPhotoState.remoteUrl ?: ""

    companion object {
        fun fromPlayer(player: PlayerResponse) = PlayerFormState(
            firstName = player.firstName,
            lastName = player.lastName,
            documentNumber = player.documentNumber,
            age = player.age.toString(),
            dateOfBirth = player.dateOfBirth,
            photoState = PhotoPickerState(remoteUrl = player.photoUrl),
            dniFrontPhotoState = PhotoPickerState(remoteUrl = player.documentFrontUrl),
            dniBackPhotoState = PhotoPickerState(remoteUrl = player.documentBackUrl),
            isEditing = true,
            editingPlayerId = player.id,
            clubId = player.clubId,
            position = player.position
        )
    }
}
