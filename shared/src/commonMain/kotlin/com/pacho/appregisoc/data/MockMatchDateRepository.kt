package com.pacho.appregisoc.data

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.ClubResponse
import com.pacho.appregisoc.data.dto.MatchDateResponse
import com.pacho.appregisoc.data.dto.MatchDateStatus
import com.pacho.appregisoc.domain.repository.MatchDateRepository
import kotlinx.coroutines.delay

class MockMatchDateRepository : MatchDateRepository {

    override suspend fun getByEvent(eventId: Long): Result<List<MatchDateResponse>> {
        return try {
            delay(600L)
            Result.Success(mockMatchDates.filter { it.eventId == eventId })
        } catch (e: Exception) {
            Result.Error("Error al obtener fechas: ${e.message}", e)
        }
    }

    override suspend fun getById(id: Long): Result<MatchDateResponse?> {
        return try {
            delay(300L)
            Result.Success(mockMatchDates.firstOrNull { it.id == id })
        } catch (e: Exception) {
            Result.Error("Error al obtener fecha: ${e.message}", e)
        }
    }

    private companion object {
        val homeClub = ClubResponse(
            id = 1,
            name = "Club Deportivo Estrella",
            foundedYear = 1950,
            description = "Club histórico de la ciudad",
            createdBy = "Admin",
            isActive = true,
            createdAt = "2024-01-15T10:30:00",
            updatedAt = "2026-06-20T14:45:00"
        )

        val awayClub = ClubResponse(
            id = 2,
            name = "Club Atlético Rival",
            foundedYear = 1975,
            description = "Club fundado en la zona norte",
            createdBy = "Admin",
            isActive = true,
            createdAt = "2024-02-10T09:00:00",
            updatedAt = "2026-06-25T11:20:00"
        )

        val mockMatchDates = listOf(
            MatchDateResponse(
                id = 1,
                eventId = 1,
                date = "2026-08-05",
                startTime = "15:30",
                location = "Estadio Central",
                status = MatchDateStatus.SCHEDULED,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-07-20T10:30:00",
                updatedAt = "2026-07-25T14:45:00"
            ),
            MatchDateResponse(
                id = 2,
                eventId = 1,
                date = "2026-08-12",
                startTime = "16:00",
                location = "Complejo Arenales",
                status = MatchDateStatus.SCHEDULED,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-07-20T10:31:00",
                updatedAt = "2026-07-25T14:46:00"
            ),
            MatchDateResponse(
                id = 3,
                eventId = 1,
                date = "2026-08-19",
                startTime = "15:00",
                location = "Estadio Central",
                status = MatchDateStatus.ONGOING,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-07-20T10:32:00",
                updatedAt = "2026-08-19T15:00:00"
            ),
            MatchDateResponse(
                id = 4,
                eventId = 1,
                date = "2026-08-26",
                startTime = "17:00",
                location = "Polideportivo Sur",
                status = MatchDateStatus.FINISHED,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-07-20T10:33:00",
                updatedAt = "2026-08-26T19:00:00"
            ),
            MatchDateResponse(
                id = 5,
                eventId = 1,
                date = "2026-09-02",
                startTime = "15:30",
                location = "Estadio Central",
                status = MatchDateStatus.CANCELLED,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-07-20T10:34:00",
                updatedAt = "2026-08-30T12:00:00"
            ),
            MatchDateResponse(
                id = 6,
                eventId = 2,
                date = "2026-09-20",
                startTime = "18:00",
                location = "Complejo Arenales",
                status = MatchDateStatus.SCHEDULED,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-08-01T10:00:00",
                updatedAt = "2026-08-10T10:00:00"
            ),
            MatchDateResponse(
                id = 7,
                eventId = 2,
                date = "2026-09-27",
                startTime = "18:00",
                location = "Estadio Central",
                status = MatchDateStatus.SCHEDULED,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-08-01T10:00:00",
                updatedAt = "2026-08-10T10:00:00"
            ),
            MatchDateResponse(
                id = 8,
                eventId = 3,
                date = "2026-07-12",
                startTime = "14:00",
                location = "Estadio Central",
                status = MatchDateStatus.FINISHED,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-05-01T10:00:00",
                updatedAt = "2026-07-12T16:00:00"
            ),
            MatchDateResponse(
                id = 9,
                eventId = 3,
                date = "2026-07-19",
                startTime = "14:00",
                location = "Complejo Arenales",
                status = MatchDateStatus.FINISHED,
                homeClub = homeClub,
                awayClub = awayClub,
                createdAt = "2026-05-01T10:00:00",
                updatedAt = "2026-07-19T16:00:00"
            )
        )
    }
}
