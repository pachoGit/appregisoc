package com.pacho.appregisoc.domain.repository

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.EventRequest
import com.pacho.appregisoc.data.dto.EventResponse
import com.pacho.appregisoc.data.dto.EventUpdateRequest

interface EventRepository {
    suspend fun getByClub(clubId: Long): Result<List<EventResponse>>
    suspend fun getById(id: Long): Result<EventResponse?>
    suspend fun createEvent(request: EventRequest): Result<EventResponse>
    suspend fun updateEvent(id: Long, request: EventUpdateRequest): Result<Unit>
    suspend fun deleteEvent(id: Long): Result<Unit>
}