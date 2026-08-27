// port-lint: source tracing-subscriber/src/fmt/time/datetime.rs
@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlin.time.Clock

/**
 * Formats timestamps as RFC3339 / ISO-8601 date-time strings.
 */
class DateTimeFormat : FormatTime {
    override fun formatTime(writer: StringBuilder) {
        val now = Clock.System.now()
        writer.append(now.toString()).append(" ")
    }
}
