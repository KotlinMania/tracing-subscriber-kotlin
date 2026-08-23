package io.github.kotlinmania.tracingsubscriber.fmt

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Record
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer
import io.github.kotlinmania.tracingsubscriber.layer.with
import io.github.kotlinmania.tracingsubscriber.registry.Registry
import io.github.kotlinmania.tracingsubscriber.registry.registry

/**
 * Formatter for span and event key-value fields.
 */
interface FormatFields {
    fun formatFields(writer: StringBuilder, fields: Map<String, Any?>)
}

/**
 * Default field formatter emitting fields as `key=value`.
 */
object DefaultFields : FormatFields {
    override fun formatFields(writer: StringBuilder, fields: Map<String, Any?>) {
        var first = true
        for ((key, value) in fields) {
            if (!first) {
                writer.append(" ")
            }
            first = false
            writer.append(key).append("=").append(value.toString())
        }
    }
}

/**
 * Compact field formatter.
 */
object CompactFields : FormatFields {
    override fun formatFields(writer: StringBuilder, fields: Map<String, Any?>) {
        var first = true
        for ((key, value) in fields) {
            if (!first) {
                writer.append(", ")
            }
            first = false
            writer.append(key).append(": ").append(value.toString())
        }
    }
}

/**
 * JSON field formatter.
 */
object JsonFields : FormatFields {
    override fun formatFields(writer: StringBuilder, fields: Map<String, Any?>) {
        var first = true
        writer.append("{")
        for ((key, value) in fields) {
            if (!first) {
                writer.append(",")
            }
            first = false
            writer.append("\"").append(key).append("\":")
            when (value) {
                null -> writer.append("null")
                is Number, is Boolean -> writer.append(value.toString())
                else -> writer.append("\"").append(value.toString().replace("\"", "\\\"")).append("\"")
            }
        }
        writer.append("}")
    }
}

/**
 * Context provided to event formatters.
 */
class FmtContext<S : Subscriber>(
    val context: Context<S>,
    val formatFields: FormatFields = DefaultFields,
)

/**
 * Formats a tracing [Event] to a [StringBuilder].
 */
interface FormatEvent<S : Subscriber> {
    fun formatEvent(fmtContext: FmtContext<S>, writer: StringBuilder, event: Event)
}

/**
 * Full, human-readable single-line event formatter.
 */
class FullFormatEvent<S : Subscriber>(
    var displayTimestamp: Boolean = false,
    var displayTarget: Boolean = true,
    var displayLevel: Boolean = true,
) : FormatEvent<S> {
    override fun formatEvent(fmtContext: FmtContext<S>, writer: StringBuilder, event: Event) {
        if (displayLevel) {
            writer
                .append(
                    event.metadata.level.name
                        .padEnd(5),
                ).append(" ")
        }

        val scope = fmtContext.context.eventScope(event)
        if (scope != null) {
            for (span in scope.fromRoot()) {
                writer.append(span.name)
                if (span.fields.isNotEmpty()) {
                    writer.append("{")
                    val spanFields = StringBuilder()
                    fmtContext.formatFields.formatFields(spanFields, span.fields)
                    writer.append(spanFields)
                    writer.append("}")
                }
                writer.append(": ")
            }
        }

        if (displayTarget) {
            writer.append(event.metadata.target).append(": ")
        }

        val fields = StringBuilder()
        fmtContext.formatFields.formatFields(fields, event.fields)
        writer.append(fields)
    }
}

/**
 * Compact event formatter.
 */
class CompactFormatEvent<S : Subscriber>(
    var displayLevel: Boolean = true,
) : FormatEvent<S> {
    override fun formatEvent(fmtContext: FmtContext<S>, writer: StringBuilder, event: Event) {
        if (displayLevel) {
            writer
                .append(
                    event.metadata.level.name
                        .first(),
                ).append(" ")
        }
        val fields = StringBuilder()
        fmtContext.formatFields.formatFields(fields, event.fields)
        writer.append(fields)
    }
}

/**
 * Newline-delimited JSON event formatter.
 */
class JsonFormatEvent<S : Subscriber>(
    var flattenEvent: Boolean = false,
) : FormatEvent<S> {
    override fun formatEvent(fmtContext: FmtContext<S>, writer: StringBuilder, event: Event) {
        writer.append("{")
        writer.append("\"level\":\"").append(event.metadata.level.name).append("\",")
        writer.append("\"target\":\"").append(event.metadata.target).append("\",")
        writer.append("\"fields\":")
        val fields = StringBuilder()
        fmtContext.formatFields.formatFields(fields, event.fields)
        writer.append(fields)
        writer.append("}")
    }
}

/**
 * Layer that formats events and writes them to an output sink.
 */
class FmtLayer<S : Subscriber>(
    var formatEvent: FormatEvent<S> = FullFormatEvent(),
    var formatFields: FormatFields = DefaultFields,
    var writer: (String) -> Unit = {},
) : Layer<S> {
    override fun onEvent(event: Event, context: Context<S>) {
        val buffer = StringBuilder()
        val fmtContext = FmtContext(context, formatFields)
        formatEvent.formatEvent(fmtContext, buffer, event)
        writer(buffer.toString())
    }

    override fun onNewSpan(attributes: Attributes, id: SpanId, context: Context<S>) {
        val span = context.lookupSpan(id) ?: return
        span.fields.putAll(attributes.values)
    }

    override fun onRecord(id: SpanId, values: Record, context: Context<S>) {
        val span = context.lookupSpan(id) ?: return
        span.fields.putAll(values.values)
    }
}

/**
 * Builder for configuring and creating a formatting [Subscriber].
 */
class SubscriberBuilder(
    private var maxLevel: LevelFilter = LevelFilter.INFO,
    private var displayTarget: Boolean = true,
    private var displayLevel: Boolean = true,
    private var isCompact: Boolean = false,
    private var isJson: Boolean = false,
    private var writer: (String) -> Unit = {},
) {
    fun withMaxLevel(level: LevelFilter): SubscriberBuilder {
        this.maxLevel = level
        return this
    }

    fun withTarget(displayTarget: Boolean): SubscriberBuilder {
        this.displayTarget = displayTarget
        return this
    }

    fun withLevel(displayLevel: Boolean): SubscriberBuilder {
        this.displayLevel = displayLevel
        return this
    }

    fun compact(): SubscriberBuilder {
        this.isCompact = true
        this.isJson = false
        return this
    }

    fun json(): SubscriberBuilder {
        this.isJson = true
        this.isCompact = false
        return this
    }

    fun withWriter(writer: (String) -> Unit): SubscriberBuilder {
        this.writer = writer
        return this
    }

    fun finish(): Subscriber {
        val registry = registry()
        val fieldFormat: FormatFields =
            if (isJson) {
                JsonFields
            } else if (isCompact) {
                CompactFields
            } else {
                DefaultFields
            }
        val eventFormat: FormatEvent<Registry> =
            when {
                isJson -> JsonFormatEvent()
                isCompact -> CompactFormatEvent(displayLevel = displayLevel)
                else -> FullFormatEvent(displayTarget = displayTarget, displayLevel = displayLevel)
            }
        val layer =
            FmtLayer(
                formatEvent = eventFormat,
                formatFields = fieldFormat,
                writer = writer,
            )
        return registry.with(layer)
    }
}

/**
 * Entry point for building a formatting subscriber.
 */
fun fmt(): SubscriberBuilder = SubscriberBuilder()
