package com.pacho.appregisoc.ui.features.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.pacho.appregisoc.domain.model.Player
import com.pacho.appregisoc.ui.components.PlayerCard

@Composable
fun PlayerListScreen(
    uiState: PlayerUiState,
    onAddPlayer: () -> Unit,
    onEditPlayer: (Player) -> Unit,
    onDeletePlayer: (String) -> Unit,
    onViewPlayer: (Player) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPlayer,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "Agregar jugador"
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
                is PlayerUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is PlayerUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is PlayerUiState.Success -> {
                    val players = uiState.players
                    if (players.isEmpty()) {
                        Text(
                            text = "No hay jugadores registrados",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(players, key = { it.id }) { player ->
                                PlayerCard(
                                    player = player,
                                    onEdit = { onEditPlayer(player) },
                                    onDelete = { onDeletePlayer(player.id) },
                                    onView = { onViewPlayer(player) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val mockPlayers = listOf(
    Player("1", "Juan", "Pérez", "12345678", 946684800000L, 25),
    Player("2", "María", "García", "87654321", 883612800000L, 28)
)

@Preview
@Composable
private fun PlayerListScreenSuccessPreview() {
    MaterialTheme {
        PlayerListScreen(
            uiState = PlayerUiState.Success(mockPlayers),
            onAddPlayer = {},
            onEditPlayer = {},
            onDeletePlayer = {},
            onViewPlayer = {}
        )
    }
}

@Preview
@Composable
private fun PlayerListScreenEmptyPreview() {
    MaterialTheme {
        PlayerListScreen(
            uiState = PlayerUiState.Success(emptyList()),
            onAddPlayer = {},
            onEditPlayer = {},
            onDeletePlayer = {},
            onViewPlayer = {}
        )
    }
}

@Preview
@Composable
private fun PlayerListScreenErrorPreview() {
    MaterialTheme {
        PlayerListScreen(
            uiState = PlayerUiState.Error("Error al cargar jugadores"),
            onAddPlayer = {},
            onEditPlayer = {},
            onDeletePlayer = {},
            onViewPlayer = {}
        )
    }
}

@Preview
@Composable
private fun PlayerListScreenLoadingPreview() {
    MaterialTheme {
        PlayerListScreen(
            uiState = PlayerUiState.Loading,
            onAddPlayer = {},
            onEditPlayer = {},
            onDeletePlayer = {},
            onViewPlayer = {}
        )
    }
}
