package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.LineupResponse
import com.pacho.appregisoc.data.dto.SetLineupRequest
import com.pacho.appregisoc.domain.repository.LineupRepository
import com.pacho.appregisoc.domain.validation.LineupValidator

class SaveLineupUseCase(
    private val repository: LineupRepository
) {
    suspend operator fun invoke(
        id: Long?,
        matchId: Long,
        clubId: Long,
        playerIds: List<Long>,
        coachId: Long?,
        physicalTrainerId: Long?
    ): Result<LineupResponse> {
        val validation = LineupValidator.validate(playerIds)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        val request = SetLineupRequest(
            matchId = matchId,
            clubId = clubId,
            playerIds = playerIds.distinct(),
            coachId = coachId,
            physicalTrainerId = physicalTrainerId
        )

        return if (id == null) {
            repository.create(request)
        } else {
            repository.update(id, request)
        }
    }
}
