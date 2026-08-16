package com.pacho.appregisoc.ui.features.coach

import com.pacho.appregisoc.data.dto.CoachResponse
import com.pacho.appregisoc.ui.common.PhotoFormState
import com.pacho.appregisoc.ui.components.PhotoPickerState

data class CoachFormState(
    override val photoState: PhotoPickerState = PhotoPickerState(),
    override val dniFrontPhotoState: PhotoPickerState = PhotoPickerState(),
    override val dniBackPhotoState: PhotoPickerState = PhotoPickerState(),
    val firstName: String = "",
    val lastName: String = "",
    val documentNumber: String = "",
    val age: String = "",
    val dateOfBirth: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingCoachId: Long? = null,
    val clubId: Long = 1L
) : PhotoFormState {
    val photoUrl: String get() = photoState.remoteUrl ?: ""
    val documentFrontUrl: String get() = dniFrontPhotoState.remoteUrl ?: ""
    val documentBackUrl: String get() = dniBackPhotoState.remoteUrl ?: ""

    companion object {
        fun fromCoach(coach: CoachResponse) = CoachFormState(
            firstName = coach.firstName,
            lastName = coach.lastName,
            documentNumber = coach.documentNumber,
            age = coach.age.toString(),
            dateOfBirth = coach.dateOfBirth,
            photoState = PhotoPickerState(remoteUrl = coach.photoUrl),
            dniFrontPhotoState = PhotoPickerState(remoteUrl = coach.documentFrontUrl),
            dniBackPhotoState = PhotoPickerState(remoteUrl = coach.documentBackUrl),
            isEditing = true,
            editingCoachId = coach.id,
            clubId = coach.clubId
        )
    }
}
