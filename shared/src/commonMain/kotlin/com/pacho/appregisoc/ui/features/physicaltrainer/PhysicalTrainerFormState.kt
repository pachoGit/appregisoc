package com.pacho.appregisoc.ui.features.physicaltrainer

import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse

data class PhysicalTrainerFormState(
    val firstName: String = "",
    val lastName: String = "",
    val documentNumber: String = "",
    val age: String = "",
    val dateOfBirth: String = "",
    val photoUrl: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingId: Long? = null,
    val clubId: Long = 1L
) {
    companion object {
        fun fromPhysicalTrainer(trainer: PhysicalTrainerResponse) = PhysicalTrainerFormState(
            firstName = trainer.firstName,
            lastName = trainer.lastName,
            documentNumber = trainer.documentNumber,
            age = trainer.age.toString(),
            dateOfBirth = trainer.dateOfBirth,
            photoUrl = trainer.photoUrl ?: "",
            isEditing = true,
            editingId = trainer.id,
            clubId = trainer.clubId
        )
    }
}
