// port-lint: source tracing-subscriber/src/filter/filter_fn.rs
package io.github.kotlinmania.tracingsubscriber.filter

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer

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
