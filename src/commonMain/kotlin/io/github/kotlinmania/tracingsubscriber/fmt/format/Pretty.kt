// port-lint: source fmt/format/pretty.rs
package io.github.kotlinmania.tracingsubscriber.fmt.format

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.fmt.writer.Writer

/**
 * An excessively pretty, human-readable event formatter.
 */
class PrettyFormat(
    var displayLocation: Boolean = true,
) {
    fun formatEvent(event: Event, writer: Writer) {
        val sb = StringBuilder()
        sb.append("  ").append(event.metadata.level.name).append(" ")
        sb.append(event.metadata.target).append(": ")
        val message = event.fields["message"] ?: ""
        sb.append(message)

        val otherFields = event.fields.filterKeys { it != "message" }
        if (otherFields.isNotEmpty()) {
            sb.append(", ")
            sb.append(otherFields.entries.joinToString(", ") { "${it.key}: ${it.value}" })
        }
        writer.writeLine(sb.toString())
    }
}
