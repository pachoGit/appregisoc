package com.pacho.appregisoc.domain.usecase

import com.pacho.appregisoc.core.Result
import com.pacho.appregisoc.core.UuidGenerator
import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.domain.model.PlayerValidator
import com.pacho.appregisoc.domain.repository.PlayerRepository

class SavePlayerUseCase(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(
        id: String?,
        firstNames: String,
        lastNames: String,
        dni: String,
        age: String,
        birthDate: Long,
        photoUrl: String?,
        dniFrontPhotoUrl: String?,
        dniBackPhotoUrl: String?
    ): Result<Unit> {
        val validation = PlayerValidator.validate(firstNames, lastNames, dni, age, birthDate)
        if (!validation.isValid) {
            return Result.Error(validation.errors.values.joinToString("\n"))
        }

        val player = Player(
            id = id ?: UuidGenerator.generate(),
            firstNames = firstNames.trim(),
            lastNames = lastNames.trim(),
            dni = dni.trim(),
            age = age.toInt(),
            birthDate = birthDate,
            photoUrl = photoUrl?.ifBlank { null },
            dniFrontPhotoUrl = dniFrontPhotoUrl?.ifBlank { null },
            dniBackPhotoUrl = dniBackPhotoUrl?.ifBlank { null }
        )

        return repository.savePlayer(player)
    }
}
