// port-lint: source layer/mod.rs
package io.github.kotlinmania.tracingsubscriber.layer

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Record
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.Filter
import io.github.kotlinmania.tracingsubscriber.filter.layerfilters.Filtered

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

    fun <F : Filter<S>> withFilter(filter: F): Filtered<Layer<S>, F, S> = Filtered(this, filter)
}

/**
 * Extension trait adding `with` method to compose [Subscriber] with [Layer]s.
 */
interface SubscriberExt : Subscriber

/**
 * Extension on [Subscriber] to compose it with a [Layer].
 */
fun <S : Subscriber, L : Layer<S>> S.with(layer: L): LayeredSubscriber<L, S> =
    LayeredSubscriber(layer, this)
