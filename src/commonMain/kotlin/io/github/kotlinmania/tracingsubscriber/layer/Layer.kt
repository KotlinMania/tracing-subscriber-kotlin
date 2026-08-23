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
import io.github.kotlinmania.tracingsubscriber.registry.Scope
import io.github.kotlinmania.tracingsubscriber.registry.SpanData
import io.github.kotlinmania.tracingsubscriber.registry.SpanRef

/**
 * Context provided to a [Layer] to interact with the underlying [Subscriber].
 */
class Context<out S : Subscriber>(
    val subscriber: S?,
    val filterId: Long = 0L,
) {
    fun currentSpan(): SpanId? = subscriber?.currentSpan()

    fun enabled(metadata: Metadata): Boolean = subscriber?.enabled(metadata) ?: true

    fun event(event: Event) {
        subscriber?.event(event)
    }

    fun lookupSpan(id: SpanId): SpanRef<LookupSpan>? = (subscriber as? LookupSpan)?.span(id)

    fun lookupCurrent(): SpanRef<LookupSpan>? = currentSpan()?.let { lookupSpan(it) }

    fun eventSpan(event: Event): SpanRef<LookupSpan>? =
        when {
            event.isRoot -> null
            event.parent != null -> lookupSpan(event.parent)
            event.isContextual -> lookupCurrent()
            else -> null
        }

    fun eventScope(event: Event): Scope<LookupSpan>? = eventSpan(event)?.scope()

    fun metadata(id: SpanId): Metadata? = lookupSpan(id)?.metadata

    fun exists(id: SpanId): Boolean = lookupSpan(id) != null

    fun spanScope(id: SpanId): Scope<LookupSpan>? = lookupSpan(id)?.scope()
}

/**
 * Composable component that decorates or processes tracing events and span lifecycles.
 */
interface Layer<S : Subscriber> {
    fun registerCallsite(metadata: Metadata): Interest = Interest.ALWAYS

    fun enabled(metadata: Metadata, context: Context<S>): Boolean = true

    fun onNewSpan(attributes: Attributes, id: SpanId, context: Context<S>) {}

    fun onRecord(id: SpanId, values: Record, context: Context<S>) {}

    fun onFollowsFrom(span: SpanId, follows: SpanId, context: Context<S>) {}

    fun eventEnabled(event: Event, context: Context<S>): Boolean = enabled(event.metadata, context)

    fun onEvent(event: Event, context: Context<S>) {}

    fun onEnter(id: SpanId, context: Context<S>) {}

    fun onExit(id: SpanId, context: Context<S>) {}

    fun onClose(id: SpanId, context: Context<S>) {}

    fun onIdChange(oldId: SpanId, newId: SpanId, context: Context<S>) {}

    fun maxLevelHint(): LevelFilter? = null

    fun <L : Layer<S>> andThen(other: L): Layered<Layer<S>, L, S> = Layered(this, other)

    fun withSubscriber(subscriber: S): LayeredSubscriber<Layer<S>, S> = LayeredSubscriber(this, subscriber)
}

/**
 * Combines two [Layer]s into a single composed layer.
 */
class Layered<A : Layer<S>, B : Layer<S>, S : Subscriber>(
    val layerA: A,
    val layerB: B,
) : Layer<S> {
    override fun registerCallsite(metadata: Metadata): Interest {
        val interestA = layerA.registerCallsite(metadata)
        if (interestA.isNever()) return Interest.NEVER
        val interestB = layerB.registerCallsite(metadata)
        return when {
            interestB.isNever() -> Interest.NEVER
            interestA.isSometimes() || interestB.isSometimes() -> Interest.SOMETIMES
            else -> Interest.ALWAYS
        }
    }

    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        layerA.enabled(metadata, context) && layerB.enabled(metadata, context)

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
        layerA.eventEnabled(event, context) && layerB.eventEnabled(event, context)

    override fun onEvent(event: Event, context: Context<S>) {
        layerA.onEvent(event, context)
        layerB.onEvent(event, context)
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
            hintA != null && hintB != null -> if (hintA.priority > hintB.priority) hintA else hintB
            hintA != null -> hintA
            else -> hintB
        }
    }
}

/**
 * Wraps a [Subscriber] and layers a [Layer] on top of it.
 */
class LayeredSubscriber<L : Layer<S>, S : Subscriber>(
    val layer: L,
    val inner: S,
) : Subscriber,
    LookupSpan {
    private fun context(): Context<S> = Context(inner)

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
        layer.enabled(metadata, context()) && inner.enabled(metadata)

    override fun newSpan(attributes: Attributes): SpanId {
        val id = inner.newSpan(attributes)
        layer.onNewSpan(attributes, id, context())
        return id
    }

    override fun record(id: SpanId, values: Record) {
        inner.record(id, values)
        layer.onRecord(id, values, context())
    }

    override fun recordFollowsFrom(span: SpanId, follows: SpanId) {
        inner.recordFollowsFrom(span, follows)
        layer.onFollowsFrom(span, follows, context())
    }

    override fun eventEnabled(event: Event): Boolean =
        layer.eventEnabled(event, context()) && inner.eventEnabled(event)

    override fun event(event: Event) {
        inner.event(event)
        layer.onEvent(event, context())
    }

    override fun enter(id: SpanId) {
        inner.enter(id)
        layer.onEnter(id, context())
    }

    override fun exit(id: SpanId) {
        inner.exit(id)
        layer.onExit(id, context())
    }

    override fun currentSpan(): SpanId? = inner.currentSpan()

    override fun cloneSpan(id: SpanId): SpanId {
        val newId = inner.cloneSpan(id)
        if (newId != id) {
            layer.onIdChange(id, newId, context())
        }
        return newId
    }

    override fun tryClose(id: SpanId): Boolean {
        val closed = inner.tryClose(id)
        if (closed) {
            layer.onClose(id, context())
        }
        return closed
    }

    override fun maxLevelHint(): LevelFilter? {
        val layerHint = layer.maxLevelHint()
        val innerHint = inner.maxLevelHint()
        return when {
            layerHint != null && innerHint != null -> if (layerHint.priority > innerHint.priority) layerHint else innerHint
            layerHint != null -> layerHint
            else -> innerHint
        }
    }

    override fun spanData(id: SpanId): SpanData? = (inner as? LookupSpan)?.spanData(id)

    override fun span(id: SpanId): SpanRef<LookupSpan>? = (inner as? LookupSpan)?.span(id)

    override fun registerFilter(): Long = (inner as? LookupSpan)?.registerFilter() ?: 0L
}

/**
 * Extension on [Subscriber] to compose it with a [Layer].
 */
fun <S : Subscriber, L : Layer<S>> S.with(layer: L): LayeredSubscriber<L, S> =
    LayeredSubscriber(layer, this)
