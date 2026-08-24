// port-lint: source fmt/mod.rs
package io.github.kotlinmania.tracingsubscriber.fmt

import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.fmt.format.Format
import io.github.kotlinmania.tracingsubscriber.fmt.format.FormatEvent
import io.github.kotlinmania.tracingsubscriber.fmt.format.Full
import io.github.kotlinmania.tracingsubscriber.fmt.writer.MakeWriter
import io.github.kotlinmania.tracingsubscriber.fmt.writer.StdoutWriter
import io.github.kotlinmania.tracingsubscriber.layer.LayeredSubscriber
import io.github.kotlinmania.tracingsubscriber.registry.Registry
import io.github.kotlinmania.tracingsubscriber.setGlobalDefault

/**
 * A builder for creating configured [Subscriber] instances that format events.
 */
class SubscriberBuilder(
    private var makeWriter: MakeWriter = MakeWriter { StdoutWriter() },
    private var fmtEvent: FormatEvent = Format<Full>(),
    private var isAnsi: Boolean = true,
) {
    fun withWriter(makeWriter: MakeWriter): SubscriberBuilder {
        this.makeWriter = makeWriter
        return this
    }

    fun eventFormat(fmtEvent: FormatEvent): SubscriberBuilder {
        this.fmtEvent = fmtEvent
        return this
    }

    fun withAnsi(isAnsi: Boolean): SubscriberBuilder {
        this.isAnsi = isAnsi
        return this
    }

    fun withTarget(displayTarget: Boolean): SubscriberBuilder {
        (fmtEvent as? Format<*>)?.withTarget(displayTarget)
        return this
    }

    fun withLevel(displayLevel: Boolean): SubscriberBuilder {
        (fmtEvent as? Format<*>)?.withLevel(displayLevel)
        return this
    }

    fun finish(): LayeredSubscriber<io.github.kotlinmania.tracingsubscriber.layer.Layer<Registry>, Registry> {
        val fmtLayer = Layer<Registry>(makeWriter = makeWriter, fmtEvent = fmtEvent, isAnsi = isAnsi)
        return LayeredSubscriber(fmtLayer, Registry())
    }

    fun init() {
        setGlobalDefault(finish())
    }
}

fun fmt(): SubscriberBuilder = SubscriberBuilder()

fun init() {
    fmt().init()
}
