package io.github.kotlinmania.tracingsubscriber.filter

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Record
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer

/**
 * Predicate used to filter spans and events.
 */
interface Filter<in S : Subscriber> {
    fun enabled(metadata: Metadata, context: Context<S>): Boolean

    fun callsiteEnabled(metadata: Metadata): Interest = Interest.ALWAYS

    fun eventEnabled(event: Event, context: Context<S>): Boolean = enabled(event.metadata, context)

    fun maxLevelHint(): LevelFilter? = null
}

/**
 * Filter based on a predicate function on [Metadata].
 */
class FilterFn(
    val predicate: (Metadata) -> Boolean,
    var levelHint: LevelFilter? = null,
) : Filter<Subscriber>,
    Layer<Subscriber> {
    override fun enabled(metadata: Metadata, context: Context<Subscriber>): Boolean = predicate(metadata)

    override fun callsiteEnabled(metadata: Metadata): Interest =
        if (predicate(metadata)) Interest.ALWAYS else Interest.NEVER

    override fun eventEnabled(event: Event, context: Context<Subscriber>): Boolean =
        enabled(event.metadata, context)

    override fun maxLevelHint(): LevelFilter? = levelHint
}

/**
 * Filter based on a dynamic predicate function on [Metadata] and [Context].
 */
class DynFilterFn<S : Subscriber>(
    val predicate: (Metadata, Context<S>) -> Boolean,
    var callsitePredicate: ((Metadata) -> Interest)? = null,
    var levelHint: LevelFilter? = null,
) : Filter<S>,
    Layer<S> {
    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        predicate(metadata, context)

    override fun callsiteEnabled(metadata: Metadata): Interest =
        callsitePredicate?.invoke(metadata) ?: Interest.ALWAYS

    override fun eventEnabled(event: Event, context: Context<S>): Boolean =
        enabled(event.metadata, context)

    override fun maxLevelHint(): LevelFilter? = levelHint
}

fun filterFn(predicate: (Metadata) -> Boolean): FilterFn = FilterFn(predicate)

fun <S : Subscriber> dynamicFilterFn(predicate: (Metadata, Context<S>) -> Boolean): DynFilterFn<S> =
    DynFilterFn(predicate)

/**
 * Wraps a [Layer] with a [Filter].
 */
class FilteredLayer<L : Layer<S>, F : Filter<S>, S : Subscriber>(
    val layer: L,
    val filter: F,
) : Layer<S> {
    override fun registerCallsite(metadata: Metadata): Interest {
        val filterInterest = filter.callsiteEnabled(metadata)
        if (filterInterest.isNever()) return Interest.NEVER
        val layerInterest = layer.registerCallsite(metadata)
        return when {
            layerInterest.isNever() -> Interest.NEVER
            filterInterest.isSometimes() || layerInterest.isSometimes() -> Interest.SOMETIMES
            else -> Interest.ALWAYS
        }
    }

    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        filter.enabled(metadata, context) && layer.enabled(metadata, context)

    override fun onNewSpan(attributes: Attributes, id: SpanId, context: Context<S>) {
        if (filter.enabled(attributes.metadata, context)) {
            layer.onNewSpan(attributes, id, context)
        }
    }

    override fun onRecord(id: SpanId, values: Record, context: Context<S>) {
        layer.onRecord(id, values, context)
    }

    override fun onFollowsFrom(span: SpanId, follows: SpanId, context: Context<S>) {
        layer.onFollowsFrom(span, follows, context)
    }

    override fun eventEnabled(event: Event, context: Context<S>): Boolean =
        filter.eventEnabled(event, context) && layer.eventEnabled(event, context)

    override fun onEvent(event: Event, context: Context<S>) {
        if (filter.eventEnabled(event, context)) {
            layer.onEvent(event, context)
        }
    }

    override fun onEnter(id: SpanId, context: Context<S>) {
        layer.onEnter(id, context)
    }

    override fun onExit(id: SpanId, context: Context<S>) {
        layer.onExit(id, context)
    }

    override fun onClose(id: SpanId, context: Context<S>) {
        layer.onClose(id, context)
    }

    override fun onIdChange(oldId: SpanId, newId: SpanId, context: Context<S>) {
        layer.onIdChange(oldId, newId, context)
    }

    override fun maxLevelHint(): LevelFilter? {
        val filterHint = filter.maxLevelHint()
        val layerHint = layer.maxLevelHint()
        return when {
            filterHint != null && layerHint != null ->
                if (filterHint.priority < layerHint.priority) filterHint else layerHint
            filterHint != null -> filterHint
            else -> layerHint
        }
    }
}

fun <S : Subscriber, L : Layer<S>, F : Filter<S>> L.withFilter(filter: F): FilteredLayer<L, F, S> =
    FilteredLayer(this, filter)
