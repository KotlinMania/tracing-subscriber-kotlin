// port-lint: source tracing-subscriber/src/fmt/format/mod.rs
package io.github.kotlinmania.tracingsubscriber.fmt.format

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.field.RecordFields
import io.github.kotlinmania.tracingsubscriber.field.Visit
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
interface FormatEvent {
    fun formatEvent(event: Event, writer: Writer)

    companion object {
        inline operator fun invoke(crossinline block: (Event, Writer) -> Unit): FormatEvent =
            object : FormatEvent {
                override fun formatEvent(event: Event, writer: Writer) = block(event, writer)
            }
    }
}

/**
 * A type that can format a set of fields to a [Writer].
 */
interface FormatFields {
    fun formatFields(writer: Writer, fields: RecordFields)

    fun addFields(current: StringBuilder, fields: RecordFields) {
        if (current.isNotEmpty()) {
            current.append(' ')
        }
        val w = Writer { current.append(it) }
        formatFields(w, fields)
    }
}

/**
 * The default [FormatFields] implementation.
 */
class DefaultFields : FormatFields {
    override fun formatFields(writer: Writer, fields: RecordFields) {
        val visitor = DefaultVisitor(writer)
        fields.record(visitor)
    }
}

/**
 * The visitor produced by [DefaultFields].
 */
class DefaultVisitor(
    private val writer: Writer,
) : Visit {
    private var isFirst = true

    private fun writeField(name: String, value: Any?) {
        if (!isFirst) {
            writer.write(" ")
        }
        isFirst = false
        writer.write("$name=$value")
    }

    override fun recordF64(name: String, value: Double) = writeField(name, value)

    override fun recordI64(name: String, value: Long) = writeField(name, value)

    override fun recordU64(name: String, value: ULong) = writeField(name, value)

    override fun recordBool(name: String, value: Boolean) = writeField(name, value)

    override fun recordStr(name: String, value: String) = writeField(name, value)

    override fun recordDebug(name: String, value: Any?) = writeField(name, value)
}

/**
 * A [FormatFields] implementation that formats fields by calling a function or closure.
 */
class FieldFn(
    val f: (Writer, RecordFields) -> Unit,
) : FormatFields {
    override fun formatFields(writer: Writer, fields: RecordFields) {
        f(writer, fields)
    }
}

class FieldFnVisitor<F>(
    val f: F,
    val writer: Writer,
)

class ErrorSourceList(
    val error: Throwable,
)

class FmtCtx<S, N>(
    val ctx: Any? = null,
    val span: Any? = null,
    val ansi: Boolean = true,
)

class Style {
    fun bold(): Style = this

    fun paint(d: Any?): String = d?.toString() ?: ""

    fun prefix(): String = ""

    fun suffix(): String = ""
}

class FmtThreadName(
    val name: String,
)

class FmtLevel(
    val level: Level,
    val ansi: Boolean = false,
)

/**
 * Configures what points in the span lifecycle are logged as events.
 */
data class FmtSpan(
    val bits: Int,
) {
    fun contains(other: FmtSpan): Boolean = (bits and other.bits) == other.bits

    infix fun and(other: FmtSpan): FmtSpan = FmtSpan(bits and other.bits)

    infix fun or(other: FmtSpan): FmtSpan = FmtSpan(bits or other.bits)

    infix fun xor(other: FmtSpan): FmtSpan = FmtSpan(bits xor other.bits)

    companion object {
        val NONE = FmtSpan(0)
        val NEW = FmtSpan(1 shl 0)
        val ENTER = FmtSpan(1 shl 1)
        val EXIT = FmtSpan(1 shl 2)
        val CLOSE = FmtSpan(1 shl 3)
        val ACTIVE = FmtSpan(ENTER.bits or EXIT.bits)
        val FULL = FmtSpan(NEW.bits or ENTER.bits or EXIT.bits or CLOSE.bits)
    }
}

class FmtSpanConfig(
    val kind: FmtSpan = FmtSpan.NONE,
    val fmtTiming: Boolean = true,
) {
    fun withoutTime(): FmtSpanConfig = FmtSpanConfig(kind, false)

    fun withKind(kind: FmtSpan): FmtSpanConfig = FmtSpanConfig(kind, fmtTiming)

    fun traceNew(): Boolean = kind.contains(FmtSpan.NEW)

    fun traceEnter(): Boolean = kind.contains(FmtSpan.ENTER)

    fun traceExit(): Boolean = kind.contains(FmtSpan.EXIT)

    fun traceClose(): Boolean = kind.contains(FmtSpan.CLOSE)
}

class TimingDisplay(
    val nanos: Long,
)

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
