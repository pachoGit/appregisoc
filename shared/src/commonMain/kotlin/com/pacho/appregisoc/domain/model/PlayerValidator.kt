package com.pacho.appregisoc.domain.model

import com.pacho.appregisoc.core.ValidationResult

object PlayerValidator {

    fun validate(
        firstNames: String,
        lastNames: String,
        dni: String,
        age: String,
        birthDate: Long
    ): ValidationResult {
        val errors = mutableMapOf<String, String>()

        if (firstNames.isBlank()) {
            errors["firstNames"] = "El nombre es obligatorio"
        } else if (firstNames.length < 2) {
            errors["firstNames"] = "El nombre debe tener al menos 2 caracteres"
        }

        if (lastNames.isBlank()) {
            errors["lastNames"] = "Los apellidos son obligatorios"
        } else if (lastNames.length < 2) {
            errors["lastNames"] = "Los apellidos deben tener al menos 2 caracteres"
        }

        if (dni.isBlank()) {
            errors["dni"] = "El DNI es obligatorio"
        } else if (!dni.matches(Regex("^\\d{8}$"))) {
            errors["dni"] = "El DNI debe tener 8 dígitos"
        }

        val ageInt = age.toIntOrNull()
        if (ageInt == null || ageInt < 0 || ageInt > 120) {
            errors["age"] = "Ingrese una edad válida (0-120)"
        }

        if (birthDate <= 0L) {
            errors["birthDate"] = "Seleccione una fecha de nacimiento"
        }

        return if (errors.isEmpty()) {
            ValidationResult.valid()
        } else {
            ValidationResult.invalid(errors)
        }
    }
}
