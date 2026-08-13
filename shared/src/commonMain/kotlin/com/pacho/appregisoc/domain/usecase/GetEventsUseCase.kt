package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.core.map
import com.pacho.appregisoc.data.dto.EventResponse
import com.pacho.appregisoc.data.dto.EventStatus
import com.pacho.appregisoc.domain.repository.EventRepository

class GetEventsUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(clubId: Long): Result<List<EventResponse>> {
        return repository.getByClub(clubId).map { events ->
            events.sortedWith(
                compareBy<EventResponse> { it.status.order }
                    .thenBy { it.startDate }
                    .thenBy { it.name }
            )
        }
    }
}

private val EventStatus.order: Int
    get() = when (this) {
        EventStatus.ONGOING -> 0
        EventStatus.UPCOMING -> 1
        EventStatus.FINISHED -> 2
    }