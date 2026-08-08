package com.pacho.appregisoc.ui.features.player

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun Long.toDateString(): String {
    return try {
        val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)
        val day = localDateTime.day.toString().padStart(2, '0')
        val month = localDateTime.month.number.toString().padStart(2, '0')
        val year = localDateTime.year
        "$year-$month-$day"
    } catch (e: Exception) {
        ""
    }
}
