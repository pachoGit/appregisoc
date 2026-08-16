package com.pacho.appregisoc.data

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.CoachResponse
import com.pacho.appregisoc.data.dto.MatchDateResponse
import com.pacho.appregisoc.domain.repository.MatchDateRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.isSuccess

class MatchDateApiService(
    private val client: HttpClient,
    private val baseUrl: String
): MatchDateRepository {

    override suspend fun getByEventAndClub(
        eventId: Long,
        clubId: Long
    ): Result<List<MatchDateResponse>> {
        return try {
            val response = client.get {
                url("$baseUrl/by-event/$eventId/club/$clubId")
            }
            if (!response.status.isSuccess()) {
                return Result.Error("Error al obtener fechas: ${response.status}")
            }
            Result.Success(response.body<List<MatchDateResponse>>())
        } catch (e: Exception) {
            Result.Error("Error al obtener fechas: ${e.message}", e)
        }
    }
}