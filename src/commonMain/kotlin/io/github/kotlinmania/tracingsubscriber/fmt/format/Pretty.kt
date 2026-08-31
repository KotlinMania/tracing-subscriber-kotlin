// port-lint: source tracing-subscriber/src/fmt/format/pretty.rs
package io.github.kotlinmania.tracingsubscriber.fmt.format

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.field.RecordFields
import io.github.kotlinmania.tracingsubscriber.field.Visit
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

/**
 * An excessively pretty, human-readable field formatter.
 */
class PrettyFields(
    var ansi: Boolean? = null,
) : FormatFields {
    override fun formatFields(writer: Writer, fields: RecordFields) {
        val visitor = PrettyVisitor(writer)
        fields.record(visitor)
    }

    companion object {
        fun new(): PrettyFields = PrettyFields()
    }
}

class PrettyVisitor(
    val writer: Writer,
) : Visit {
    private var isFirst = true

    private fun writeField(name: String, value: Any?) {
        if (!isFirst) {
            writer.write(", ")
        }
        isFirst = false
        writer.write("$name: $value")
    }

    override fun recordF64(name: String, value: Double) = writeField(name, value)

    override fun recordI64(name: String, value: Long) = writeField(name, value)

    override fun recordU64(name: String, value: ULong) = writeField(name, value)

    override fun recordBool(name: String, value: Boolean) = writeField(name, value)

    override fun recordStr(name: String, value: String) = writeField(name, value)

    override fun recordDebug(name: String, value: Any?) = writeField(name, value)
}
