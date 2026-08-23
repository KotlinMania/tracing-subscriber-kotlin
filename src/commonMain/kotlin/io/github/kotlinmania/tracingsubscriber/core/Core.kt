package io.github.kotlinmania.tracingsubscriber.core

/**
 * Severity level for spans and events.
 */
enum class Level(
    val priority: Int,
) : Comparable<Level> {
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4),
    TRACE(5),
    ;

    companion object {
        fun fromString(value: String): Level? =
            when (value.trim().lowercase()) {
                "trace" -> TRACE
                "debug" -> DEBUG
                "info" -> INFO
                "warn", "warning" -> WARN
                "error" -> ERROR
                else -> null
            }
    }
}

/**
 * Filter level indicating the maximum enabled verbosity.
 */
enum class LevelFilter(
    val priority: Int,
) : Comparable<LevelFilter> {
    OFF(0),
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4),
    TRACE(5),
    ;

    operator fun contains(level: Level): Boolean = this.priority >= level.priority

    fun toLevel(): Level? =
        when (this) {
            OFF -> null
            ERROR -> Level.ERROR
            WARN -> Level.WARN
            INFO -> Level.INFO
            DEBUG -> Level.DEBUG
            TRACE -> Level.TRACE
        }

    companion object {
        fun fromLevel(level: Level): LevelFilter =
            when (level) {
                Level.TRACE -> TRACE
                Level.DEBUG -> DEBUG
                Level.INFO -> INFO
                Level.WARN -> WARN
                Level.ERROR -> ERROR
            }

        fun fromString(value: String): LevelFilter =
            when (value.trim().lowercase()) {
                "off" -> OFF
                "error", "1" -> ERROR
                "warn", "warning", "2" -> WARN
                "info", "3" -> INFO
                "debug", "4" -> DEBUG
                "trace", "5" -> TRACE
                else -> throw IllegalArgumentException("Invalid level filter: $value")
            }

        fun fromStringOrNull(value: String): LevelFilter? =
            try {
                fromString(value)
            } catch (_: IllegalArgumentException) {
                null
            }
    }
}

/**
 * Call-site and event interest cache state.
 */
enum class Interest {
    NEVER,
    SOMETIMES,
    ALWAYS,
    ;

    fun isNever(): Boolean = this == NEVER

    fun isSometimes(): Boolean = this == SOMETIMES

    fun isAlways(): Boolean = this == ALWAYS
}

/**
 * Static metadata associated with a trace call-site, span, or event.
 */
data class Metadata(
    val name: String,
    val target: String,
    val level: Level,
    val fields: List<String> = emptyList(),
    val isSpan: Boolean = false,
    val isEvent: Boolean = false,
    val file: String? = null,
    val line: Int? = null,
)

/**
 * Unique identifier for a span within an active trace session.
 */
data class SpanId(
    val value: Long,
)

/**
 * Attributes used to create a new span.
 */
data class Attributes(
    val metadata: Metadata,
    val parent: SpanId? = null,
    val isRoot: Boolean = false,
    val isContextual: Boolean = true,
    val values: Map<String, Any?> = emptyMap(),
)

/**
 * Key-value record updates for an active span.
 */
data class Record(
    val values: Map<String, Any?> = emptyMap(),
)

/**
 * An event emitted during tracing.
 */
data class Event(
    val metadata: Metadata,
    val parent: SpanId? = null,
    val isRoot: Boolean = false,
    val isContextual: Boolean = true,
    val fields: Map<String, Any?> = emptyMap(),
)

/**
 * Core interface for recording and consuming diagnostic trace events and spans.
 */
interface Subscriber {
    fun registerCallsite(metadata: Metadata): Interest = Interest.ALWAYS

    fun enabled(metadata: Metadata): Boolean = true

    fun newSpan(attributes: Attributes): SpanId

    fun record(id: SpanId, values: Record) {}

    fun recordFollowsFrom(span: SpanId, follows: SpanId) {}

    fun eventEnabled(event: Event): Boolean = enabled(event.metadata)

    fun event(event: Event) {}

    fun enter(id: SpanId) {}

    fun exit(id: SpanId) {}

    fun currentSpan(): SpanId? = null

    fun cloneSpan(id: SpanId): SpanId = id

    fun tryClose(id: SpanId): Boolean = true

    fun maxLevelHint(): LevelFilter? = null
}
