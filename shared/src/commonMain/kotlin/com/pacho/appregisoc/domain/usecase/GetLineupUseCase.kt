package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.LineupResponse
import com.pacho.appregisoc.domain.repository.LineupRepository

class GetLineupUseCase(
    private val repository: LineupRepository
) {
    suspend operator fun invoke(matchId: Long, clubId: Long): Result<LineupResponse?> {
        return repository.getByMatch(matchId, clubId)
    }
}
