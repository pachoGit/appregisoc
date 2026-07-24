package com.pacho.appregisoc.ui.features.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.domain.model.PlayerValidator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerFormScreen(
    player: Player? = null,
    onSave: (PlayerFormState) -> Unit,
    onCancel: () -> Unit
) {
    var formState by remember {
        mutableStateOf(
            if (player != null) PlayerFormState.fromPlayer(player)
            else PlayerFormState()
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (formState.birthDate == 0L) null else formState.birthDate
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    formState = formState.copy(
                        birthDate = datePickerState.selectedDateMillis ?: 0L
                    )
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (formState.isEditing) "Editar Jugador"
                        else "Registrar Jugador"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancelar"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val validation = PlayerValidator.validate(
                            firstNames = formState.firstNames,
                            lastNames = formState.lastNames,
                            dni = formState.dni,
                            age = formState.age,
                            birthDate = formState.birthDate
                        )
                        if (validation.isValid) {
                            onSave(formState)
                        } else {
                            formState = formState.copy(errors = validation.errors)
                        }
                    }) {
                        Text(
                            "Guardar",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Información Obligatoria",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = formState.firstNames,
                    onValueChange = {
                        formState = formState.copy(firstNames = it, errors = formState.errors - "firstNames")
                    },
                    label = { Text("Nombres") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = formState.errors.containsKey("firstNames"),
                    supportingText = formState.errors["firstNames"]?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )

                OutlinedTextField(
                    value = formState.lastNames,
                    onValueChange = {
                        formState = formState.copy(lastNames = it, errors = formState.errors - "lastNames")
                    },
                    label = { Text("Apellidos") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = formState.errors.containsKey("lastNames"),
                    supportingText = formState.errors["lastNames"]?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )

                OutlinedTextField(
                    value = formState.dni,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() } && it.length <= 8) {
                            formState = formState.copy(dni = it, errors = formState.errors - "dni")
                        }
                    },
                    label = { Text("DNI") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = formState.errors.containsKey("dni"),
                    supportingText = formState.errors["dni"]?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )

                OutlinedTextField(
                    value = formState.age,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() } && it.length <= 3) {
                            formState = formState.copy(age = it, errors = formState.errors - "age")
                        }
                    },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = formState.errors.containsKey("age"),
                    supportingText = formState.errors["age"]?.let {
                        { Text(it, color = MaterialTheme.colorScheme.error) }
                    }
                )

                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (formState.errors.containsKey("birthDate"))
                            MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Fecha de Nacimiento",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            if (formState.birthDate == 0L) "Seleccionar"
                            else "Seleccionada",
                            color = if (formState.birthDate == 0L)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Fotos (Opcional - URLs)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = formState.photoUrl,
                    onValueChange = { formState = formState.copy(photoUrl = it) },
                    label = { Text("URL Foto del Jugador") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = formState.dniFrontPhotoUrl,
                    onValueChange = { formState = formState.copy(dniFrontPhotoUrl = it) },
                    label = { Text("URL Foto Frontal DNI") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = formState.dniBackPhotoUrl,
                    onValueChange = { formState = formState.copy(dniBackPhotoUrl = it) },
                    label = { Text("URL Foto Posterior DNI") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    )
}
