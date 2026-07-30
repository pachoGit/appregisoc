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
import com.pacho.appregisoc.domain.model.CoachValidator
import com.pacho.appregisoc.ui.components.FormScaffold
import com.pacho.appregisoc.ui.features.player.toDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachCreateScreen(
    onSave: (CoachFormState) -> Unit,
    onCancel: () -> Unit
) {
    var formState by remember { mutableStateOf(CoachFormState()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

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
        title = "Registrar Entrenador",
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
private fun CoachCreateScreenPreview() {
    MaterialTheme { CoachCreateScreen(onSave = {}, onCancel = {}) }
}
