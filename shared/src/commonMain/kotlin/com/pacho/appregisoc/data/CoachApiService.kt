package com.pacho.appregisoc.data

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.CoachRequest
import com.pacho.appregisoc.data.dto.CoachResponse
import com.pacho.appregisoc.data.dto.CoachUpdateRequest
import com.pacho.appregisoc.domain.repository.CoachRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CoachApiService(
    private val client: HttpClient,
    private val baseUrl: String = "http://localhost:8080/api/coaches"
) : CoachRepository {

    private val coachesFlow = MutableStateFlow<List<CoachResponse>>(emptyList())

    override fun getCoaches(): Flow<List<CoachResponse>> = coachesFlow.asStateFlow()

    override suspend fun getById(id: Long): Result<CoachResponse?> {
        return try {
            val response = client.get {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener entrenador: ${response.status}")
            }
            Result.Success(response.body<CoachResponse>())
        } catch (e: Exception) {
            Result.Error("Error al obtener entrenador: ${e.message}", e)
        }
    }

    override suspend fun getByClub(clubId: Long): Result<List<CoachResponse>> {
        return try {
            val response = client.get {
                url(baseUrl) {
                    parameters.append("clubId", clubId.toString())
                }
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener entrenadores: ${response.status}")
            }
            val coaches = response.body<List<CoachResponse>>()
            coachesFlow.value = coaches
            Result.Success(coaches)
        } catch (e: Exception) {
            Result.Error("Error al obtener entrenadores: ${e.message}", e)
        }
    }

    override suspend fun createCoach(coach: CoachResponse): Result<CoachResponse> {
        return try {
            val response = client.post {
                url(baseUrl)
                contentType(ContentType.Application.Json)
                setBody(
                    CoachRequest(
                        clubId = coach.clubId,
                        firstName = coach.firstName,
                        lastName = coach.lastName,
                        documentNumber = coach.documentNumber,
                        age = coach.age,
                        dateOfBirth = coach.dateOfBirth,
                        photoUrl = coach.photoUrl
                    )
                )
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al crear entrenador: ${response.status}")
            }
            val created = response.body<CoachResponse>()
            coachesFlow.value = coachesFlow.value + created
            Result.Success(created)
        } catch (e: Exception) {
            Result.Error("Error al crear entrenador: ${e.message}", e)
        }
    }

    override suspend fun updateCoach(id: Long, coach: CoachResponse): Result<Unit> {
        return try {
            val response = client.put {
                url("$baseUrl/$id")
                contentType(ContentType.Application.Json)
                setBody(
                    CoachUpdateRequest(
                        firstName = coach.firstName,
                        lastName = coach.lastName,
                        documentNumber = coach.documentNumber,
                        age = coach.age,
                        dateOfBirth = coach.dateOfBirth,
                        photoUrl = coach.photoUrl
                    )
                )
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al actualizar entrenador: ${response.status}")
            }
            coachesFlow.value = coachesFlow.value.map {
                if (it.id == id) coach else it
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al actualizar entrenador: ${e.message}", e)
        }
    }

    override suspend fun deleteCoach(id: Long): Result<Unit> {
        return try {
            val response = client.delete {
                url("$baseUrl/$id")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al eliminar entrenador: ${response.status}")
            }
            coachesFlow.value = coachesFlow.value.filter { it.id != id }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error al eliminar entrenador: ${e.message}", e)
        }
    }
}
