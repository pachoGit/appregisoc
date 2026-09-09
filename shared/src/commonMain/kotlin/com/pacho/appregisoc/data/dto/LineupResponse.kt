package com.pacho.appregisoc.data.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LineupStatus {
    @SerialName("DRAFT") DRAFT,
    @SerialName("CLOSED") CLOSED
}

@Serializable
data class LineupResponse(
    val id: Long,
    val matchId: Long,
    val clubId: Long,
    val players: List<LineupPlayerResponse> = emptyList(),
    val coach: LineupCoachResponse? = null,
    val physicalTrainer: LineupPhysicalTrainerResponse? = null,
    @SerialName("createdAt")
    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    val createdAt: LocalDateTime,
    @SerialName("updatedAt")
    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    val updatedAt: LocalDateTime
) {
    val playerIds: Set<Long>
        get() = players.map { it.playerId }.toSet()
}

@Serializable
data class LineupPlayerResponse(
    val playerId: Long,
    val firstName: String,
    val lastName: String,
    val documentNumber: String,
    val age: Int,
    val dateOfBirth: String,
    val position: PlayerPosition? = null,
    val photoUrl: String? = null
)

@Serializable
data class LineupCoachResponse(
    val coachId: Long,
    val firstName: String,
    val lastName: String,
    val documentNumber: String,
    val age: Int,
    val dateOfBirth: String,
    val photoUrl: String? = null
)

@Serializable
data class LineupPhysicalTrainerResponse(
    val physicalTrainerId: Long,
    val firstName: String,
    val lastName: String,
    val documentNumber: String,
    val age: Int,
    val dateOfBirth: String,
    val photoUrl: String? = null
)
