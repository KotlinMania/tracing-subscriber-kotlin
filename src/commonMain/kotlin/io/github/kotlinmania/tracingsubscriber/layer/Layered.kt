// port-lint: source tracing-subscriber/src/layer/layered.rs
package io.github.kotlinmania.tracingsubscriber.layer

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Record
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.registry.LookupSpan
import io.github.kotlinmania.tracingsubscriber.registry.SpanData
import io.github.kotlinmania.tracingsubscriber.registry.SpanRef

/**
 * Combines two [Layer]s into a single composed layer.
 */
class Layered<A : Layer<S>, B : Layer<S>, S : Subscriber>(
    val layerA: A,
    val layerB: B,
) : Layer<S> {
    override fun registerCallsite(metadata: Metadata): Interest {
        val interestA = layerA.registerCallsite(metadata)
        val interestB = layerB.registerCallsite(metadata)
        return when {
            interestA.isNever() && interestB.isNever() -> Interest.NEVER
            interestA.isAlways() || interestB.isAlways() -> Interest.ALWAYS
            else -> Interest.SOMETIMES
        }
    }

    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        layerA.enabled(metadata, context) || layerB.enabled(metadata, context)

    override fun onNewSpan(attributes: Attributes, id: SpanId, context: Context<S>) {
        layerA.onNewSpan(attributes, id, context)
        layerB.onNewSpan(attributes, id, context)
    }

    override fun onRecord(id: SpanId, values: Record, context: Context<S>) {
        layerA.onRecord(id, values, context)
        layerB.onRecord(id, values, context)
    }

    override fun onFollowsFrom(span: SpanId, follows: SpanId, context: Context<S>) {
        layerA.onFollowsFrom(span, follows, context)
        layerB.onFollowsFrom(span, follows, context)
    }

    override fun eventEnabled(event: Event, context: Context<S>): Boolean =
        layerA.eventEnabled(event, context) || layerB.eventEnabled(event, context)

    override fun onEvent(event: Event, context: Context<S>) {
        if (layerA.eventEnabled(event, context)) {
            layerA.onEvent(event, context)
        }
        if (layerB.eventEnabled(event, context)) {
            layerB.onEvent(event, context)
        }
    }

    override fun onEnter(id: SpanId, context: Context<S>) {
        layerA.onEnter(id, context)
        layerB.onEnter(id, context)
    }

    override fun onExit(id: SpanId, context: Context<S>) {
        layerA.onExit(id, context)
        layerB.onExit(id, context)
    }

    override fun onClose(id: SpanId, context: Context<S>) {
        layerA.onClose(id, context)
        layerB.onClose(id, context)
    }

    override fun onIdChange(oldId: SpanId, newId: SpanId, context: Context<S>) {
        layerA.onIdChange(oldId, newId, context)
        layerB.onIdChange(oldId, newId, context)
    }

    override fun maxLevelHint(): LevelFilter? {
        val hintA = layerA.maxLevelHint()
        val hintB = layerB.maxLevelHint()
        return when {
            hintA == null -> hintB
            hintB == null -> hintA
            hintA.priority >= hintB.priority -> hintA
            else -> hintB
        }
    }
}

/**
 * Combines a [Layer] with a [Subscriber] to produce a new [Subscriber].
 */
class LayeredSubscriber<L : Layer<S>, S : Subscriber>(
    val layer: L,
    val inner: S,
) : Subscriber,
    LookupSpan {
    val context: Context<S> = Context(inner)

    override fun registerCallsite(metadata: Metadata): Interest {
        val layerInterest = layer.registerCallsite(metadata)
        if (layerInterest.isNever()) return Interest.NEVER
        val innerInterest = inner.registerCallsite(metadata)
        return when {
            innerInterest.isNever() -> Interest.NEVER
            layerInterest.isSometimes() || innerInterest.isSometimes() -> Interest.SOMETIMES
            else -> Interest.ALWAYS
        }
    }

    override fun enabled(metadata: Metadata): Boolean =
        layer.enabled(metadata, context) && inner.enabled(metadata)

    override fun newSpan(attributes: Attributes): SpanId {
        val id = inner.newSpan(attributes)
        layer.onNewSpan(attributes, id, context)
        return id
    }

    override fun record(id: SpanId, values: Record) {
        inner.record(id, values)
        layer.onRecord(id, values, context)
    }

    override fun recordFollowsFrom(span: SpanId, follows: SpanId) {
        inner.recordFollowsFrom(span, follows)
        layer.onFollowsFrom(span, follows, context)
    }

    override fun event(event: Event) {
        if (layer.eventEnabled(event, context)) {
            layer.onEvent(event, context)
        }
        inner.event(event)
    }

    override fun enter(id: SpanId) {
        inner.enter(id)
        layer.onEnter(id, context)
    }

    override fun exit(id: SpanId) {
        layer.onExit(id, context)
        inner.exit(id)
    }

    override fun cloneSpan(id: SpanId): SpanId = inner.cloneSpan(id)

    override fun tryClose(id: SpanId): Boolean {
        val closed = inner.tryClose(id)
        if (closed) {
            layer.onClose(id, context)
        }
        return closed
    }

    override fun currentSpan(): SpanId? = inner.currentSpan()

    override fun spanData(id: SpanId): SpanData? = (inner as? LookupSpan)?.spanData(id)

    override fun span(id: SpanId): SpanRef<LookupSpan>? = (inner as? LookupSpan)?.span(id)

    override fun registerFilter(): Long = (inner as? LookupSpan)?.registerFilter() ?: 0L
}
