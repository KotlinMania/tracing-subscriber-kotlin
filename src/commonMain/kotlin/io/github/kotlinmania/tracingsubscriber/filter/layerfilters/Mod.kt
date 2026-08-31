// port-lint: source tracing-subscriber/src/filter/layer_filters/mod.rs
package io.github.kotlinmania.tracingsubscriber.filter.layerfilters

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
import io.github.kotlinmania.tracingsubscriber.layer.Layer

/**
 * Uniquely identifies an individual [Filter] instance in the context of a [Subscriber].
 */
data class FilterId(
    val id: Long,
)

/**
 * Bitset mapping for per-layer filter results.
 */
data class FilterMap(
    var bits: ULong = 0uL,
) {
    fun isEnabled(id: FilterId): Boolean = (bits and (1uL shl id.id.toInt())) == 0uL

    fun disable(id: FilterId) {
        bits = bits or (1uL shl id.id.toInt())
    }

    fun enable(id: FilterId) {
        bits = bits and (1uL shl id.id.toInt()).inv()
    }
}

class FilterState(
    var enabled: FilterMap = FilterMap(),
    var interest: Interest? = null,
    val counters: DebugCounters = DebugCounters(),
)

class DebugCounters(
    var inFilterPass: Int = 0,
    var inCallsitePass: Int = 0,
)

class MagicPlfDowncastMarker(
    val id: FilterId,
)

class FmtBitset(
    val bits: ULong,
)

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

    override fun maxLevelHint(): LevelFilter? = filter.maxLevelHint() ?: layer.maxLevelHint()
}
