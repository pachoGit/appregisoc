package com.pacho.appregisoc.ui.features.event

import com.pacho.appregisoc.data.dto.EventResponse
import com.pacho.appregisoc.data.dto.EventStatus

data class EventFormState(
    val clubId: Long = 1L,
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val status: EventStatus = EventStatus.UPCOMING,
    val errors: Map<String, String> = emptyMap(),
    val isEditing: Boolean = false,
    val editingEventId: Long? = null
) {
    companion object {
        fun fromEvent(event: EventResponse) = EventFormState(
            // clubId = event.clubId,
            title = event.name,
            description = event.description ?: "",
            location = event.location ?: "",
            startDate = event.startDate,
            endDate = event.endDate ?: "",
            status = event.status,
            isEditing = true,
            editingEventId = event.id
        )
    }
}