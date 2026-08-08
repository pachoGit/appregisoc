package com.pacho.appregisoc.ui.features.physicaltrainer

import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse
import com.pacho.appregisoc.ui.components.PhotoPickerState

data class PhysicalTrainerFormState(
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
    val editingId: Long? = null,
    val clubId: Long = 1L
) {
    val photoUrl: String get() = photoState.remoteUrl ?: ""
    val documentFrontUrl: String get() = dniFrontPhotoState.remoteUrl ?: ""
    val documentBackUrl: String get() = dniBackPhotoState.remoteUrl ?: ""

    companion object {
        fun fromPhysicalTrainer(trainer: PhysicalTrainerResponse) = PhysicalTrainerFormState(
            firstName = trainer.firstName,
            lastName = trainer.lastName,
            documentNumber = trainer.documentNumber,
            age = trainer.age.toString(),
            dateOfBirth = trainer.dateOfBirth,
            photoState = PhotoPickerState(remoteUrl = trainer.photoUrl),
            dniFrontPhotoState = PhotoPickerState(remoteUrl = trainer.documentFrontUrl),
            dniBackPhotoState = PhotoPickerState(remoteUrl = trainer.documentBackUrl),
            isEditing = true,
            editingId = trainer.id,
            clubId = trainer.clubId
        )
    }
}
