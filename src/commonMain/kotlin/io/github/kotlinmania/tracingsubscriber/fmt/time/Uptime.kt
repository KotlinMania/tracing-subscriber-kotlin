@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlin.time.TimeMark
import kotlin.time.TimeSource

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

    companion object {
        fun default(): Uptime = Uptime()

        fun from(mark: TimeMark): Uptime = Uptime(mark)
    }
}

fun uptime(): Uptime = Uptime()
