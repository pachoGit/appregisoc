package com.pacho.appregisoc.ui.features.physicaltrainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse
import com.pacho.appregisoc.ui.components.InfoRow
import com.pacho.appregisoc.ui.components.InfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhysicalTrainerDetailScreen(
    entity: PhysicalTrainerResponse,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Preparador Físico") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${entity.firstName} ${entity.lastName}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(title = "Datos Personales") {
                InfoRow(label = "DNI", value = entity.documentNumber)
                InfoRow(label = "Edad", value = "${entity.age} años")
                InfoRow(label = "Fecha de Nacimiento", value = entity.dateOfBirth.ifBlank { "No registrada" })
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private val previewTrainer = PhysicalTrainerResponse(
    id = 1, clubId = 1,
    firstName = "Carlos", lastName = "López",
    documentNumber = "12345678",
    age = 35, dateOfBirth = "1990-03-10"
)

@Preview
@Composable
private fun PhysicalTrainerDetailScreenPreview() {
    MaterialTheme {
        PhysicalTrainerDetailScreen(
            entity = previewTrainer,
            onBack = {}
        )
    }
}
