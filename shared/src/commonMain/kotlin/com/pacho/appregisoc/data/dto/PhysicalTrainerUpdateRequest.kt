package com.pacho.appregisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhysicalTrainerUpdateRequest(
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("documentNumber") val documentNumber: String,
    val age: Int,
    @SerialName("dateOfBirth") val dateOfBirth: String,
    @SerialName("photoUrl") val photoUrl: String? = null
)
