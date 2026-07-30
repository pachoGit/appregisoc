package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse
import com.pacho.appregisoc.domain.repository.PhysicalTrainerRepository
import kotlinx.coroutines.flow.Flow

class GetPhysicalTrainersUseCase(
    private val repository: PhysicalTrainerRepository
) {
    operator fun invoke(): Flow<List<PhysicalTrainerResponse>> = repository.getPhysicalTrainers()
}
