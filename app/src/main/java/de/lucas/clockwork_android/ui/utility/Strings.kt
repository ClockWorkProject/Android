package de.lucas.clockwork_android.ui.utility

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

/**
 * Check if the provided date equals the date of today or yesterday, to show string of day instead of date instead
 */
fun String.toCurrentDate(): String {
    val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val yesterday = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(System.currentTimeMillis() - 1000 * 60 * 60 * 24),
        UTC
    ).format(format)
    val currentDate = LocalDateTime.now().toLocalDate().format(format)
    if (this == currentDate) {
        return "Heute"
    } else if (this == yesterday) {
        return "Gestern"
    }
    return this
}