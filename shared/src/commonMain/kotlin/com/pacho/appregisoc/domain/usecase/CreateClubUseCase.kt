package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.data.dto.ClubResponse
import com.pacho.appregisoc.data.dto.CreateClubRequest
import com.pacho.appregisoc.domain.validation.ClubValidator
import com.pacho.appregisoc.domain.repository.ClubRepository

class CreateClubUseCase(
    private val repository: ClubRepository
) {
    suspend operator fun invoke(
        name: String,
        foundedYear: Int?,
        crestUrl: String?,
        description: String?,
        createdBy: String = "Admin"
    ): Result<ClubResponse> {
        val validation = ClubValidator.validate(name)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        val request = CreateClubRequest(
            name = name.trim(),
            foundedYear = foundedYear,
            crestUrl = crestUrl?.ifBlank { null },
            description = description?.ifBlank { null },
            createdBy = createdBy.trim()
        )

        return repository.createClub(request)
    }
}
