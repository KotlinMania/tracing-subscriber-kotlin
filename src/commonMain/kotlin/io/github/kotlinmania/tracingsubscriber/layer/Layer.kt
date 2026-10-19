package io.github.kotlinmania.tracingsubscriber.layer

import io.github.kotlinmania.tracing.Attributes
import io.github.kotlinmania.tracing.Event
import io.github.kotlinmania.tracing.Id
import io.github.kotlinmania.tracing.Interest
import io.github.kotlinmania.tracing.LevelFilter
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracing.Record
import io.github.kotlinmania.tracing.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.Filter
import io.github.kotlinmania.tracingsubscriber.filter.layerfilters.Filtered

/**
 * Composable component that decorates or processes tracing events and span lifecycles.
 */
interface Layer<S : Subscriber> {
    fun registerCallsite(metadata: Metadata): Interest = Interest.ALWAYS

    fun enabled(metadata: Metadata, context: Context<S>): Boolean = true

    fun onNewSpan(attributes: Attributes, id: Id, context: Context<S>) {}

    fun onRecord(id: Id, values: Record, context: Context<S>) {}

    fun onFollowsFrom(span: Id, follows: Id, context: Context<S>) {}

    fun eventEnabled(event: Event, context: Context<S>): Boolean = enabled(event.metadata, context)

    fun onEvent(event: Event, context: Context<S>) {}

    fun onEnter(id: Id, context: Context<S>) {}

    fun onExit(id: Id, context: Context<S>) {}

    fun onClose(id: Id, context: Context<S>) {}

    fun onIdChange(oldId: Id, newId: Id, context: Context<S>) {}

    fun maxLevelHint(): LevelFilter? = null

    fun andThen(other: Layer<S>): Layered<Layer<S>, Layer<S>, S> = Layered(this, other)

    fun withSubscriber(subscriber: S): LayeredSubscriber<Layer<S>, S> = LayeredSubscriber(this, subscriber)

    fun withFilter(filter: Filter<S>): Filtered<Layer<S>, Filter<S>, S> = Filtered(this, filter)
}

/**
 * Extension trait adding `with` method to compose [Subscriber] with [Layer]s.
 */
interface SubscriberExt : Subscriber

/**
 * Extension on [Subscriber] to compose it with a [Layer].
 */
fun <S : Subscriber> S.with(layer: Layer<S>): LayeredSubscriber<Layer<S>, S> =
    LayeredSubscriber(layer, this)

/**
 * A layer that does nothing.
 */
class Identity : Layer<Subscriber>

class NoneLayerMarker

fun identity(): Identity = Identity()
