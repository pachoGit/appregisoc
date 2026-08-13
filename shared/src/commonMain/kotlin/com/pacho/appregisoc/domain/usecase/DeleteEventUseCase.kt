package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.domain.repository.EventRepository

class DeleteEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> = repository.deleteEvent(id)
}