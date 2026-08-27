// port-lint: source tracing-subscriber/src/fmt/format/json.rs
package io.github.kotlinmania.tracingsubscriber.fmt.format

import io.github.kotlinmania.tracingsubscriber.core.Event
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
