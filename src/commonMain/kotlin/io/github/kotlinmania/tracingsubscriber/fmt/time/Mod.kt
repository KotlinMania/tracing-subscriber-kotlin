// port-lint: source tracing-subscriber/src/fmt/time/mod.rs
@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlin.time.Clock
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * A type that can measure and format the current time.
 */
fun interface FormatTime {
    fun formatTime(writer: StringBuilder)
}

/**
 * Default timestamp provider using wall clock.
 */
class SystemTime : FormatTime {
    override fun formatTime(writer: StringBuilder) {
        val now = Clock.System.now()
        writer.append(now.toString()).append(" ")
    }
}

/**
 * Timestamp provider using uptime relative to construction time.
 */
class Uptime(
    private val mark: TimeMark = TimeSource.Monotonic.markNow(),
) : FormatTime {
    override fun formatTime(writer: StringBuilder) {
        val elapsed = mark.elapsedNow()
        val secs = elapsed.inWholeMilliseconds.toDouble() / 1000.0
        writer.append("${secs}s ")
    }
}

fun time(): SystemTime = SystemTime()

fun uptime(): Uptime = Uptime()
