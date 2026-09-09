package com.pacho.appregisoc.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class MeResponse(
    val id: Long,
    val name: String,
    val surname: String,
    val documentNumber: String,
    val username: String,
    val role: String,
    val club: ClubInfoResponse?
)

@Serializable
data class ClubInfoResponse(
    val id: Long,
    val name: String
)

