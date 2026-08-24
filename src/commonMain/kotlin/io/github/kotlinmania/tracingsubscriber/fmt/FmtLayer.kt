// port-lint: source fmt/fmt_layer.rs
package io.github.kotlinmania.tracingsubscriber.fmt

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.fmt.format.Format
import io.github.kotlinmania.tracingsubscriber.fmt.format.FormatEvent
import io.github.kotlinmania.tracingsubscriber.fmt.format.Full
import io.github.kotlinmania.tracingsubscriber.fmt.writer.MakeWriter
import io.github.kotlinmania.tracingsubscriber.fmt.writer.StdoutWriter
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer

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
