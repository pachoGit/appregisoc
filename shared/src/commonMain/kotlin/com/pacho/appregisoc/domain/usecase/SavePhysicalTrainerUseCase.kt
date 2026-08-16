package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.core.map
import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse
import com.pacho.appregisoc.domain.validation.PhysicalTrainerValidator
import com.pacho.appregisoc.domain.repository.PhysicalTrainerRepository

class SavePhysicalTrainerUseCase(
    private val repository: PhysicalTrainerRepository
) {
    suspend operator fun invoke(
        id: Long?,
        firstName: String,
        lastName: String,
        documentNumber: String,
        age: String,
        dateOfBirth: String,
        clubId: Long = 1L,
        photoUrl: String? = null,
        documentFrontUrl: String? = null,
        documentBackUrl: String? = null
    ): Result<Unit> {
        val validation = PhysicalTrainerValidator.validate(firstName, lastName, documentNumber, age, dateOfBirth)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        val trainer = PhysicalTrainerResponse(
            id = id ?: 0L,
            clubId = clubId,
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            documentNumber = documentNumber.trim(),
            age = age.toInt(),
            dateOfBirth = dateOfBirth,
            photoUrl = photoUrl?.ifBlank { null },
            documentFrontUrl = documentFrontUrl?.ifBlank { null },
            documentBackUrl = documentBackUrl?.ifBlank { null }
        )

        return if (id != null) {
            repository.updatePhysicalTrainer(id, trainer)
        } else {
            repository.createPhysicalTrainer(trainer).map { Unit }
        }
    }
}
