package com.pacho.appregisoc.ui.features.coach

import com.pacho.appregisoc.data.dto.CoachResponse

data class CoachFormState(
    val firstName: String = "",
    val lastName: String = "",
    val documentNumber: String = "",
    val age: String = "",
    val dateOfBirth: String = "",
    val photoUrl: String = "",
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingCoachId: Long? = null,
    val clubId: Long = 1L
) {
    companion object {
        fun fromCoach(coach: CoachResponse) = CoachFormState(
            firstName = coach.firstName,
            lastName = coach.lastName,
            documentNumber = coach.documentNumber,
            age = coach.age.toString(),
            dateOfBirth = coach.dateOfBirth,
            photoUrl = coach.photoUrl ?: "",
            isEditing = true,
            editingCoachId = coach.id,
            clubId = coach.clubId
        )
    }
}
