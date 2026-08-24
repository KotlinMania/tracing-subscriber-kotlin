// port-lint: source fmt/time/datetime.rs
@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

/**
 * Formats timestamps as RFC3339 / ISO-8601 date-time strings.
 */
class DateTimeFormat(
    val timeZone: TimeZone = TimeZone.UTC,
) : FormatTime {
    override fun formatTime(writer: StringBuilder) {
        val now = Clock.System.now()
        val local = now.toLocalDateTime(timeZone)
        writer.append(local.toString()).append(" ")
    }
}
