package com.pacho.appregisoc.ui.features.coach

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pacho.appregisoc.data.dto.CoachResponse
import com.pacho.appregisoc.domain.model.CoachValidator
import com.pacho.appregisoc.ui.components.FormScaffold
import com.pacho.appregisoc.ui.features.player.toDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachEditScreen(
    coach: CoachResponse,
    onSave: (CoachFormState) -> Unit,
    onCancel: () -> Unit
) {
    var formState by remember { mutableStateOf(CoachFormState.fromCoach(coach)) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = formState.dateOfBirth.takeIf { it.isNotBlank() }?.let { null }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    formState = formState.copy(dateOfBirth = datePickerState.selectedDateMillis?.toDateString() ?: "")
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    FormScaffold(
        title = "Editar Entrenador",
        onCancel = onCancel,
        onSave = {
            val validation = CoachValidator.validate(
                formState.firstName, formState.lastName, formState.documentNumber, formState.age, formState.dateOfBirth
            )
            if (validation.isValid) onSave(formState)
            else formState = formState.copy(errors = validation.errors)
        }
    ) {
        CoachFormBody(
            formState = formState,
            onValueChange = { formState = it },
            onShowDatePicker = { showDatePicker = true },
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Preview
@Composable
private fun CoachEditScreenPreview() {
    MaterialTheme {
        CoachEditScreen(
            coach = CoachResponse(id = 1, clubId = 1, firstName = "Carlos", lastName = "López", documentNumber = "12345678", age = 35, dateOfBirth = "1990-03-10"),
            onSave = {},
            onCancel = {}
        )
    }
}
