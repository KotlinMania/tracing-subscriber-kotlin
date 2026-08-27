// port-lint: source tracing-subscriber/src/filter/mod.rs
package io.github.kotlinmania.tracingsubscriber.filter

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.layer.Context

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
 * Extension functions for combining [Filter] instances.
 */
interface FilterExt<S : Subscriber> : Filter<S> {
    fun and(other: Filter<S>): io.github.kotlinmania.tracingsubscriber.filter.layerfilters.And<Filter<S>, Filter<S>, S> =
        io.github.kotlinmania.tracingsubscriber.filter.layerfilters
            .And(this, other)

    fun or(other: Filter<S>): io.github.kotlinmania.tracingsubscriber.filter.layerfilters.Or<Filter<S>, Filter<S>, S> =
        io.github.kotlinmania.tracingsubscriber.filter.layerfilters
            .Or(this, other)

    fun not(): io.github.kotlinmania.tracingsubscriber.filter.layerfilters.Not<Filter<S>, S> =
        io.github.kotlinmania.tracingsubscriber.filter.layerfilters
            .Not(this)
}
