// port-lint: source fmt/time/datetime.rs
@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlin.time.Clock

/**
 * Represents a parsed or calculated date-time value.
 */
data class DateTime(
    val year: Long = 0,
    val month: Int = 1,
    val day: Int = 1,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val nanos: Int = 0,
)

/**
 * Formats timestamps as RFC3339 / ISO-8601 date-time strings.
 */
class DateTimeFormat : FormatTime {
    override fun formatTime(writer: StringBuilder) {
        val now = Clock.System.now()
        writer.append(now.toString()).append(" ")
    }
}
