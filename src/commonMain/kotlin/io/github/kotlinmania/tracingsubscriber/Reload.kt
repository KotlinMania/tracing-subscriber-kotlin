// port-lint: source reload.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Record
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.Filter
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.sync.RwLock

/**
 * Handle for reloading a layer or filter.
 */
class Handle<L : io.github.kotlinmania.tracingsubscriber.layer.Layer<Subscriber>> internal constructor(
    private val inner: RwLock<L>,
) {
    fun reload(newLayer: L) {
        inner.set(newLayer)
    }

    fun modify(f: (L) -> Unit) {
        inner.write(f)
    }

    fun <R> withCurrent(f: (L) -> R): R =
        inner.read(f)
}

/**
 * Wraps a [Layer] or [Filter], allowing it to be reloaded dynamically at runtime.
 */
class ReloadLayer<L : io.github.kotlinmania.tracingsubscriber.layer.Layer<Subscriber>> internal constructor(
    private val inner: RwLock<L>,
) : io.github.kotlinmania.tracingsubscriber.layer.Layer<Subscriber> {
    companion object {
        fun <L : io.github.kotlinmania.tracingsubscriber.layer.Layer<Subscriber>> new(
            layer: L,
        ): Pair<ReloadLayer<L>, Handle<L>> {
            val lock = RwLock(layer)
            return Pair(ReloadLayer(lock), Handle(lock))
        }
    }

    override fun registerCallsite(metadata: Metadata): Interest =
        inner.read { it.registerCallsite(metadata) }

    override fun enabled(metadata: Metadata, context: Context<Subscriber>): Boolean =
        inner.read { it.enabled(metadata, context) }

    override fun onNewSpan(attributes: Attributes, id: SpanId, context: Context<Subscriber>) {
        inner.read { it.onNewSpan(attributes, id, context) }
    }

    override fun onRecord(id: SpanId, values: Record, context: Context<Subscriber>) {
        inner.read { it.onRecord(id, values, context) }
    }

    override fun onFollowsFrom(span: SpanId, follows: SpanId, context: Context<Subscriber>) {
        inner.read { it.onFollowsFrom(span, follows, context) }
    }

    override fun eventEnabled(event: Event, context: Context<Subscriber>): Boolean =
        inner.read { it.eventEnabled(event, context) }

    override fun onEvent(event: Event, context: Context<Subscriber>) {
        inner.read { it.onEvent(event, context) }
    }

    override fun onEnter(id: SpanId, context: Context<Subscriber>) {
        inner.read { it.onEnter(id, context) }
    }

    override fun onExit(id: SpanId, context: Context<Subscriber>) {
        inner.read { it.onExit(id, context) }
    }

    override fun onClose(id: SpanId, context: Context<Subscriber>) {
        inner.read { it.onClose(id, context) }
    }

    override fun onIdChange(oldId: SpanId, newId: SpanId, context: Context<Subscriber>) {
        inner.read { it.onIdChange(oldId, newId, context) }
    }

    override fun maxLevelHint(): LevelFilter? =
        inner.read { it.maxLevelHint() }
}
