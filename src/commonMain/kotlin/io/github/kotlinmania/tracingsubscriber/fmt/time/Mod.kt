// port-lint: source fmt/time/mod.rs
@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlin.time.Clock
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * A type that can measure and format the current time.
 */
interface FormatTime {
    fun formatTime(writer: StringBuilder)

    companion object {
        inline operator fun invoke(crossinline block: (StringBuilder) -> Unit): FormatTime =
            object : FormatTime {
                override fun formatTime(writer: StringBuilder) = block(writer)
            }
    }
}

/**
 * Default timestamp provider using wall clock.
 */
class SystemTime : FormatTime {
    override fun formatTime(writer: StringBuilder) {
        val now = Clock.System.now()
        writer.append(now.toString()).append(" ")
    }

    companion object {
        fun default(): SystemTime = SystemTime()
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

    companion object {
        fun default(): Uptime = Uptime()

        fun from(mark: TimeMark): Uptime = Uptime(mark)
    }
}

fun time(): SystemTime = SystemTime()

fun uptime(): Uptime = Uptime()
