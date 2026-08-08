package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse
import com.pacho.appregisoc.domain.repository.PhysicalTrainerRepository
import kotlinx.coroutines.flow.Flow

class GetPhysicalTrainersUseCase(
    private val repository: PhysicalTrainerRepository
) {
    suspend operator fun invoke(clubId: Long): Result<List<PhysicalTrainerResponse>> = repository.getByClub(clubId)
}
