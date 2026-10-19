// port-lint: source fmt/format/json.rs
package io.github.kotlinmania.tracingsubscriber.fmt.format

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer
import io.github.kotlinmania.serdejson.JsonMap
import io.github.kotlinmania.serdejson.JsonNumber
import io.github.kotlinmania.serdejson.Value
import io.github.kotlinmania.serdejson.valueToStr
import io.github.kotlinmania.tracing.Event
import io.github.kotlinmania.tracingsubscriber.field.RecordFields
import io.github.kotlinmania.tracingsubscriber.field.Visit
import io.github.kotlinmania.tracingsubscriber.fmt.writer.Writer

/**
 * Marker for [Format] that indicates that the newline-delimited JSON log format should be used.
 */
data class Json(
    var flattenEvent: Boolean = false,
    var displayCurrentSpan: Boolean = true,
    var displaySpanList: Boolean = true,
) : FormatEvent {
    fun flattenEvent(flattenEvent: Boolean) {
        this.flattenEvent = flattenEvent
    }

    fun withCurrentSpan(displayCurrentSpan: Boolean) {
        this.displayCurrentSpan = displayCurrentSpan
    }

    fun withSpanList(displaySpanList: Boolean) {
        this.displaySpanList = displaySpanList
    }

    override fun formatEvent(event: Event, writer: Writer) {
        val root = JsonMap()
        root.insert("level", Value.Str(event.metadata.level.name))
        root.insert("target", Value.Str(event.metadata.target))
        val fields = JsonMap()
        for ((k, v) in event.fields) {
            val jsonVal =
                when (v) {
                    is Boolean -> Value.Bool(v)
                    is Number -> JsonNumber.fromF64(v.toDouble())?.let { Value.Number(it) } ?: Value.Str(v.toString())
                    null -> Value.Null
                    else -> Value.Str(v.toString())
                }
            fields.insert(k, jsonVal)
        }
        root.insert("fields", Value.Object(fields))
        writer.writeLine(valueToStr(Value.Object(root)))
    }

    companion object {
        fun default(): Json = Json()
    }
}

typealias JsonFormat = Json

class SerializableContext<S, N>(
    val context: Any? = null,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val seq = serializer.serializeSeq(0).getOrThrow()
        return seq.end()
    }
}

class SerializableSpan<S, N>(
    val span: Any? = null,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val map = serializer.serializeMap(0).getOrThrow()
        return map.end()
    }
}

/**
 * The JSON [FormatFields] implementation.
 */
class JsonFields : FormatFields {
    override fun formatFields(writer: Writer, fields: RecordFields) {
        val visitor = JsonVisitor(writer)
        fields.record(visitor)
        visitor.finish()
    }

    companion object {
        fun new(): JsonFields = JsonFields()
    }
}

/**
 * The visitor produced by [JsonFields]'s [MakeVisitor] implementation.
 */
class JsonVisitor(
    private val writer: Writer,
) : Visit {
    private val values: MutableMap<String, Value> = linkedMapOf()

    override fun recordF64(name: String, value: Double) {
        values[name] = Value.Number(JsonNumber.fromF64(value) ?: JsonNumber.fromI64(0))
    }

    override fun recordI64(name: String, value: Long) {
        values[name] = Value.Number(JsonNumber.fromI64(value))
    }

    override fun recordU64(name: String, value: ULong) {
        values[name] = Value.Number(JsonNumber.fromU64(value))
    }

    override fun recordBool(name: String, value: Boolean) {
        values[name] = Value.Bool(value)
    }

    override fun recordStr(name: String, value: String) {
        values[name] = Value.Str(value)
    }

    override fun recordDebug(name: String, value: Any?) {
        values[name] = Value.Str(value?.toString() ?: "null")
    }

    fun finish() {
        val jsonMap = JsonMap()
        for ((k, v) in values) {
            jsonMap.insert(k, v)
        }
        writer.write(valueToStr(Value.Object(jsonMap)))
    }
}
