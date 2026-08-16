package com.pacho.appregisoc.domain.validation

import com.pacho.appregisoc.core.ValidationResult

object EventValidator {

    fun validate(
        title: String,
        startDate: String
    ): ValidationResult {
        val errors = mutableMapOf<String, String>()

        if (title.isBlank()) {
            errors["title"] = "El título es obligatorio"
        } else if (title.length < 3) {
            errors["title"] = "El título debe tener al menos 3 caracteres"
        }

        if (startDate.isBlank()) {
            errors["startDate"] = "La fecha de inicio es obligatoria"
        }

        return if (errors.isEmpty()) {
            ValidationResult.valid()
        } else {
            ValidationResult.invalid(errors)
        }
    }
}