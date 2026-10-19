package io.github.kotlinmania.tracingsubscriber.filter.layerfilters

import io.github.kotlinmania.tracing.Attributes
import io.github.kotlinmania.tracing.Event
import io.github.kotlinmania.tracing.Id
import io.github.kotlinmania.tracing.Interest
import io.github.kotlinmania.tracing.LevelFilter
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracing.Record
import io.github.kotlinmania.tracing.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.Filter
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer

/**
 * A [Layer] that wraps an inner [Layer] and adds a [Filter] which controls what spans and events are enabled.
 */
class Filtered<L : Layer<S>, F : Filter<S>, S : Subscriber>(
    val layer: L,
    val filter: F,
    val filterId: FilterId = FilterId(0L),
) : Layer<S> {
    override fun registerCallsite(metadata: Metadata): Interest {
        val filterInterest = filter.callsiteEnabled(metadata)
        if (filterInterest.isNever()) return Interest.NEVER
        return layer.registerCallsite(metadata)
    }

    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        filter.enabled(metadata, context) && layer.enabled(metadata, context)

    override fun onNewSpan(attributes: Attributes, id: Id, context: Context<S>) {
        if (filter.enabled(attributes.metadata, context)) {
            layer.onNewSpan(attributes, id, context)
        }
    }

    override fun onRecord(id: Id, values: Record, context: Context<S>) {
        layer.onRecord(id, values, context)
    }

    override fun onFollowsFrom(span: Id, follows: Id, context: Context<S>) {
        layer.onFollowsFrom(span, follows, context)
    }

    override fun eventEnabled(event: Event, context: Context<S>): Boolean =
        filter.eventEnabled(event, context) && layer.eventEnabled(event, context)

    override fun onEvent(event: Event, context: Context<S>) {
        if (filter.eventEnabled(event, context)) {
            layer.onEvent(event, context)
        }
    }

    override fun onEnter(id: Id, context: Context<S>) {
        layer.onEnter(id, context)
    }

    override fun onExit(id: Id, context: Context<S>) {
        layer.onExit(id, context)
    }

    override fun onClose(id: Id, context: Context<S>) {
        layer.onClose(id, context)
    }

    override fun onIdChange(oldId: Id, newId: Id, context: Context<S>) {
        layer.onIdChange(oldId, newId, context)
    }

    override fun maxLevelHint(): LevelFilter? = filter.maxLevelHint() ?: layer.maxLevelHint()
}
