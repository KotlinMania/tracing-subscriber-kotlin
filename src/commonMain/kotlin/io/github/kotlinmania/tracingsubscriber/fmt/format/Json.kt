// port-lint: source fmt/format/json.rs
package io.github.kotlinmania.tracingsubscriber.fmt.format

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.field.RecordFields
import io.github.kotlinmania.tracingsubscriber.field.Visit
import io.github.kotlinmania.tracingsubscriber.fmt.writer.Writer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

/**
 * Marker for JSON formatting of log events.
 */
class JsonFormat(
    var flattenEvent: Boolean = false,
    var displayCurrentSpan: Boolean = true,
    var displaySpanList: Boolean = true,
) {
    fun formatEvent(event: Event, writer: Writer) {
        val jsonObj =
            buildJsonObject {
                put("level", JsonPrimitive(event.metadata.level.name))
                put("target", JsonPrimitive(event.metadata.target))
                val fieldsObj =
                    buildJsonObject {
                        for ((k, v) in event.fields) {
                            put(k, JsonPrimitive(v?.toString() ?: "null"))
                        }
                    }
                put("fields", fieldsObj)
            }
        writer.writeLine(Json.encodeToString(JsonObject.serializer(), jsonObj))
    }
}

/**
 * Formats fields as JSON objects.
 */
class JsonFields : FormatFields {
    override fun formatFields(writer: Writer, fields: RecordFields) {
        val visitor = JsonVisitor(writer)
        fields.record(visitor)
    }

    companion object {
        fun new(): JsonFields = JsonFields()
    }
}

class JsonVisitor(
    val writer: Writer,
) : Visit {
    override fun recordF64(name: String, value: Double) {
        writer.write("\"$name\":$value,")
    }

    override fun recordI64(name: String, value: Long) {
        writer.write("\"$name\":$value,")
    }

    override fun recordU64(name: String, value: ULong) {
        writer.write("\"$name\":$value,")
    }

    override fun recordBool(name: String, value: Boolean) {
        writer.write("\"$name\":$value,")
    }

    override fun recordStr(name: String, value: String) {
        writer.write("\"$name\":\"$value\",")
    }

    override fun recordDebug(name: String, value: Any?) {
        writer.write("\"$name\":\"$value\",")
    }
}

class SerializableContext<S, N>(
    val context: Any? = null,
)

class SerializableSpan<S, N>(
    val span: Any? = null,
)
