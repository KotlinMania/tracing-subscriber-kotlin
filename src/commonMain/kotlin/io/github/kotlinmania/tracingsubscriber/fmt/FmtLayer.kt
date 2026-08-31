// port-lint: source tracing-subscriber/src/fmt/fmt_layer.rs
package io.github.kotlinmania.tracingsubscriber.fmt

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.fmt.format.Format
import io.github.kotlinmania.tracingsubscriber.fmt.format.FormatEvent
import io.github.kotlinmania.tracingsubscriber.fmt.format.Full
import io.github.kotlinmania.tracingsubscriber.fmt.writer.MakeWriter
import io.github.kotlinmania.tracingsubscriber.fmt.writer.StdoutWriter
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer
import io.github.kotlinmania.tracingsubscriber.registry.LookupSpan
import io.github.kotlinmania.tracingsubscriber.registry.SpanRef

/**
 * Stores the formatted fields of a span in its extensions typemap.
 */
data class FormattedFields<E>(
    var fields: String = "",
    var wasAnsi: Boolean = false,
) {
    fun isEmpty(): Boolean = fields.isEmpty()

    companion object {
        fun <E> new(fields: String): FormattedFields<E> = FormattedFields(fields)
    }
}

/**
 * Provides the current span context to a formatter.
 */
class FmtContext<S : Subscriber, N>(
    val ctx: Context<S>,
    val fmtFields: N? = null,
    val event: Event? = null,
) {
    fun <R> visitSpans(f: (SpanRef<LookupSpan>) -> R) {
        val span = ctx.lookupCurrent()
        if (span != null) {
            for (s in span.scope().fromRoot()) {
                f(s)
            }
        }
    }

    fun metadata(id: SpanId): Metadata? = ctx.metadata(id)

    fun span(id: SpanId): SpanRef<LookupSpan>? = ctx.lookupSpan(id)

    fun exists(id: SpanId): Boolean = ctx.exists(id)
}

/**
 * Stores timing information for a span.
 */
class Timings(
    var idleTime: Long = 0,
    var busyTime: Long = 0,
    var lastEntered: Long? = null,
)

/**
 * A [Layer] that logs formatted representations of tracing events.
 */

class Layer<S : Subscriber>(
    var makeWriter: MakeWriter = MakeWriter { StdoutWriter() },
    var fmtEvent: FormatEvent = Format<Full>(),
    var isAnsi: Boolean = true,
) : io.github.kotlinmania.tracingsubscriber.layer.Layer<S> {
    fun withWriter(makeWriter: MakeWriter): Layer<S> {
        this.makeWriter = makeWriter
        return this
    }

    fun eventFormat(fmtEvent: FormatEvent): Layer<S> {
        this.fmtEvent = fmtEvent
        return this
    }

    fun withAnsi(isAnsi: Boolean): Layer<S> {
        this.isAnsi = isAnsi
        return this
    }

    fun withTarget(displayTarget: Boolean): Layer<S> {
        (fmtEvent as? Format<*>)?.withTarget(displayTarget)
        return this
    }

    fun withLevel(displayLevel: Boolean): Layer<S> {
        (fmtEvent as? Format<*>)?.withLevel(displayLevel)
        return this
    }

    override fun onEvent(event: Event, context: Context<S>) {
        val writer = makeWriter.makeWriterFor(event.metadata)
        fmtEvent.formatEvent(event, writer)
    }
}

fun <S : Subscriber> layer(): Layer<S> = Layer()
