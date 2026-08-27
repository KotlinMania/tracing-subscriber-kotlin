// port-lint: source tracing-subscriber/src/fmt/format/mod.rs
package io.github.kotlinmania.tracingsubscriber.fmt.format

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.fmt.time.FormatTime
import io.github.kotlinmania.tracingsubscriber.fmt.time.SystemTime
import io.github.kotlinmania.tracingsubscriber.fmt.writer.Writer

/**
 * Format style marker: Full single-line formatter.
 */
class Full

/**
 * Format style marker: Compact single-line formatter.
 */
class Compact

/**
 * Format style marker: Multi-line pretty formatter.
 */
typealias Pretty = PrettyFormat

/**
 * Format style marker: Json formatter.
 */
typealias Json = JsonFormat

/**
 * A type that can format a tracing [Event] to a [Writer].
 */
fun interface FormatEvent {
    fun formatEvent(event: Event, writer: Writer)
}

/**
 * Standard event formatter with configurable timer, target, level, thread display options.
 */
class Format<F>(
    var timer: FormatTime = SystemTime(),
    var displayTarget: Boolean = true,
    var displayLevel: Boolean = true,
    var displayThreadNames: Boolean = false,
    var displayThreadIds: Boolean = false,
) : FormatEvent {
    fun withTimer(timer: FormatTime): Format<F> {
        this.timer = timer
        return this
    }

    fun withTarget(displayTarget: Boolean): Format<F> {
        this.displayTarget = displayTarget
        return this
    }

    fun withLevel(displayLevel: Boolean): Format<F> {
        this.displayLevel = displayLevel
        return this
    }

    override fun formatEvent(event: Event, writer: Writer) {
        val sb = StringBuilder()
        timer.formatTime(sb)

        if (displayLevel) {
            sb.append(event.metadata.level.name).append(" ")
        }
        if (displayTarget) {
            sb.append(event.metadata.target).append(": ")
        }

        val message = event.fields["message"]
        if (message != null) {
            sb.append(message)
        }

        val otherFields = event.fields.filterKeys { it != "message" }
        if (otherFields.isNotEmpty()) {
            if (message != null) sb.append(" ")
            sb.append(otherFields.entries.joinToString(" ") { "${it.key}=${it.value}" })
        }

        writer.writeLine(sb.toString())
    }
}

fun format(): Format<Full> = Format()
