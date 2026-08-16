package com.pacho.appregisoc.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MatchDateStatus {
    @SerialName("UPCOMING") UPCOMING,
    @SerialName("ONGOING") ONGOING,
    @SerialName("FINISHED") FINISHED,
    @SerialName("CANCELLED") CANCELLED
}

@Serializable
data class MatchDateResponse(
    val id: Long,
    @SerialName("eventId") val eventId: Long,
    val date: String,
    @SerialName("startTime") val startTime: String? = null,
    val location: String? = null,
    val status: MatchDateStatus = MatchDateStatus.UPCOMING,
    @SerialName("homeClub") val homeClub: ClubResponse? = null,
    @SerialName("awayClub") val awayClub: ClubResponse? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
) {
    val homeClubName: String
        get() = homeClub?.name ?: "Mi Club"

    val awayClubName: String
        get() = awayClub?.name ?: "Club Rival"
}
