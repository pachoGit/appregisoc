package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.repository.PhysicalTrainerRepository

class DeletePhysicalTrainerUseCase(
    private val repository: PhysicalTrainerRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deletePhysicalTrainer(id)
}
