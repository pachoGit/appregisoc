package com.pacho.appregisoc.ui.features.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.ui.components.DniPhotoPlaceholder
import com.pacho.appregisoc.ui.components.InfoRow
import com.pacho.appregisoc.ui.components.InfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailScreen(
    player: Player,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Jugador") },
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
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${player.firstNames} ${player.lastNames}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection(title = "Datos Personales") {
                InfoRow(label = "DNI", value = player.dni)
                InfoRow(label = "Edad", value = "${player.age} años")
                InfoRow(label = "Fecha de Nacimiento", value = formatearFecha(player.birthDate))
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(title = "Documentación") {
                Text("DNI Frontal", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                DniPhotoPlaceholder()
                Spacer(modifier = Modifier.height(16.dp))
                Text("DNI Posterior", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                DniPhotoPlaceholder()
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun formatearFecha(timestamp: Long): String {
    if (timestamp <= 0L) return "No registrada"
    return "Registrada"
}
