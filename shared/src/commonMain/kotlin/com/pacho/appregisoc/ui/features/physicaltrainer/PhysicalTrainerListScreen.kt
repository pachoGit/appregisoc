package com.pacho.appregisoc.ui.features.physicaltrainer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pacho.appregisoc.data.dto.PhysicalTrainerResponse

@Composable
fun PhysicalTrainerListScreen(
    uiState: PhysicalTrainerUiState,
    onAdd: () -> Unit,
    onEdit: (PhysicalTrainerResponse) -> Unit,
    onDelete: (Long) -> Unit,
    onView: (PhysicalTrainerResponse) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar preparador físico"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is PhysicalTrainerUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PhysicalTrainerUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is PhysicalTrainerUiState.Success -> {
                    val trainers = uiState.trainers
                    if (trainers.isEmpty()) {
                        Text(
                            text = "No hay preparadores físicos registrados",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(trainers, key = { it.id }) { trainer ->
                                PhysicalTrainerCard(
                                    trainer = trainer,
                                    onEdit = { onEdit(trainer) },
                                    onDelete = { onDelete(trainer.id) },
                                    onView = { onView(trainer) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhysicalTrainerCard(
    trainer: PhysicalTrainerResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de eliminar a ${trainer.firstName} ${trainer.lastName}?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onView() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${trainer.firstName} ${trainer.lastName}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "DNI: ${trainer.documentNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Edad: ${trainer.age} años",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Ver detalle") },
                        onClick = {
                            showMenu = false
                            onView()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            showMenu = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }
}

private val mockTrainers = listOf(
    PhysicalTrainerResponse(id = 1, clubId = 1, firstName = "Carlos", lastName = "López", documentNumber = "12345678", age = 35, dateOfBirth = "1990-03-10"),
    PhysicalTrainerResponse(id = 2, clubId = 1, firstName = "Ana", lastName = "Martínez", documentNumber = "87654321", age = 30, dateOfBirth = "1995-07-22")
)

@Preview
@Composable
private fun PhysicalTrainerListScreenSuccessPreview() {
    MaterialTheme {
        PhysicalTrainerListScreen(
            uiState = PhysicalTrainerUiState.Success(mockTrainers),
            onAdd = {},
            onEdit = {},
            onDelete = {},
            onView = {}
        )
    }
}

@Preview
@Composable
private fun PhysicalTrainerListScreenEmptyPreview() {
    MaterialTheme {
        PhysicalTrainerListScreen(
            uiState = PhysicalTrainerUiState.Success(emptyList()),
            onAdd = {},
            onEdit = {},
            onDelete = {},
            onView = {}
        )
    }
}

@Preview
@Composable
private fun PhysicalTrainerListScreenErrorPreview() {
    MaterialTheme {
        PhysicalTrainerListScreen(
            uiState = PhysicalTrainerUiState.Error("Error al cargar preparadores físicos"),
            onAdd = {},
            onEdit = {},
            onDelete = {},
            onView = {}
        )
    }
}

@Preview
@Composable
private fun PhysicalTrainerListScreenLoadingPreview() {
    MaterialTheme {
        PhysicalTrainerListScreen(
            uiState = PhysicalTrainerUiState.Loading,
            onAdd = {},
            onEdit = {},
            onDelete = {},
            onView = {}
        )
    }
}
