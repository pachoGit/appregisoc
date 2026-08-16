package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.core.map
import com.pacho.appregisoc.data.dto.MatchDateResponse
import com.pacho.appregisoc.data.dto.MatchDateStatus
import com.pacho.appregisoc.domain.repository.MatchDateRepository

class GetMatchDatesUseCase(
    private val repository: MatchDateRepository
) {
    suspend operator fun invoke(eventId: Long, clubId: Long): Result<List<MatchDateResponse>> {
        return repository.getByEventAndClub(eventId, clubId).map { matchDates ->
            matchDates.sortedWith(
                compareBy<MatchDateResponse> { it.status.order }
                    .thenBy { it.date }
                    .thenBy { it.id }
            )
        }
    }
}

private val MatchDateStatus.order: Int
    get() = when (this) {
        MatchDateStatus.UPCOMING -> 0
        MatchDateStatus.ONGOING -> 1
        MatchDateStatus.FINISHED -> 2
        MatchDateStatus.CANCELLED -> 3
    }
