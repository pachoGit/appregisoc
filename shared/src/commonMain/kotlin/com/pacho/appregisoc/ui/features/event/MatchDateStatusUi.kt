package com.pacho.appregisoc.ui.features.event

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pacho.appregisoc.data.dto.MatchDateStatus

internal val MatchDateStatus.displayLabel: String
    get() = when (this) {
        MatchDateStatus.SCHEDULED -> "Programada"
        MatchDateStatus.ONGOING -> "En curso"
        MatchDateStatus.FINISHED -> "Finalizada"
        MatchDateStatus.CANCELLED -> "Cancelada"
    }

internal val MatchDateStatus.statusColor: Color
    get() = when (this) {
        MatchDateStatus.SCHEDULED -> Color(0xFF1976D2)
        MatchDateStatus.ONGOING -> Color(0xFF2E7D32)
        MatchDateStatus.FINISHED -> Color(0xFF757575)
        MatchDateStatus.CANCELLED -> Color(0xFFC62828)
    }

internal val MatchDateStatus.statusIcon: ImageVector
    get() = when (this) {
        MatchDateStatus.SCHEDULED -> Icons.Default.Schedule
        MatchDateStatus.ONGOING -> Icons.Default.PlayArrow
        MatchDateStatus.FINISHED -> Icons.Default.CheckCircle
        MatchDateStatus.CANCELLED -> Icons.Default.Cancel
    }

@Composable
internal fun MatchDateStatusBadge(
    status: MatchDateStatus,
    modifier: Modifier = Modifier
) {
    val color = status.statusColor
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.12f),
        contentColor = color
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = status.statusIcon,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status.displayLabel,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
