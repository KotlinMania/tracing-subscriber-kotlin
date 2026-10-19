// port-lint: source filter/level.rs
package io.github.kotlinmania.tracingsubscriber.filter

import io.github.kotlinmania.tracing.Event
import io.github.kotlinmania.tracing.Interest
import io.github.kotlinmania.tracing.LevelFilter
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracing.Subscriber
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer

/**
 * Filter implementation based on maximum [LevelFilter].
 */
class LevelFilterLayer<S : Subscriber>(
    val filter: LevelFilter,
) : Layer<S>,
    Filter<S> {
    override fun registerCallsite(metadata: Metadata): Interest =
        if (filter.contains(metadata.level)) {
            Interest.ALWAYS
        } else {
            Interest.NEVER
        }

    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        filter.contains(metadata.level)

    override fun eventEnabled(event: Event, context: Context<S>): Boolean =
        filter.contains(event.metadata.level)

    override fun maxLevelHint(): LevelFilter? = filter
}
