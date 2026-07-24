package com.pacho.appregisoc.ui.features.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pacho.appregisoc.ui.components.PhotoPickerField

@Composable
fun PlayerFormBody(
    formState: PlayerFormState,
    onValueChange: (PlayerFormState) -> Unit,
    onPickImage: (PhotoType) -> Unit,
    onShowDatePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
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
                onValueChange(formState.copy(firstNames = it, errors = formState.errors - "firstNames"))
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
                onValueChange(formState.copy(lastNames = it, errors = formState.errors - "lastNames"))
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
                    onValueChange(formState.copy(dni = it, errors = formState.errors - "dni"))
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
                    onValueChange(formState.copy(age = it, errors = formState.errors - "age"))
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowDatePicker() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
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
                Text("Fecha de Nacimiento", style = MaterialTheme.typography.bodyLarge)
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
            "Fotos del Jugador",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        PhotoPickerField(
            label = "Foto del Jugador",
            state = formState.photoState,
            onPickImage = { onPickImage(PhotoType.PLAYER) }
        )

        PhotoPickerField(
            label = "DNI Frontal",
            state = formState.dniFrontPhotoState,
            onPickImage = { onPickImage(PhotoType.DNI_FRONT) }
        )

        PhotoPickerField(
            label = "DNI Posterior",
            state = formState.dniBackPhotoState,
            onPickImage = { onPickImage(PhotoType.DNI_BACK) }
        )
    }
}
