package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.EventRequest
import com.pacho.appregisoc.data.dto.EventResponse
import com.pacho.appregisoc.data.dto.EventStatus
import com.pacho.appregisoc.domain.validation.EventValidator
import com.pacho.appregisoc.domain.repository.EventRepository

class CreateEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(
        clubId: Long,
        title: String,
        description: String?,
        location: String?,
        startDate: String,
        endDate: String?,
        status: EventStatus
    ): Result<EventResponse> {
        val validation = EventValidator.validate(title = title, startDate = startDate)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        return repository.createEvent(
            EventRequest(
                clubId = clubId,
                title = title.trim(),
                description = description?.ifBlank { null },
                location = location?.ifBlank { null },
                startDate = startDate.trim(),
                endDate = endDate?.ifBlank { null },
                status = status
            )
        )
    }
}