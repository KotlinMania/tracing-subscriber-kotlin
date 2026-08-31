// port-lint: source tracing-subscriber/src/lib.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.env.EnvFilter
import io.github.kotlinmania.tracingsubscriber.layer.Layer
import io.github.kotlinmania.tracingsubscriber.registry.Registry

typealias EnvFilter = EnvFilter
typealias Layer<S> = Layer<S>
typealias Registry = Registry

var globalSubscriber: Subscriber? = null

/**
 * Sets the default global [Subscriber].
 */
fun setGlobalDefault(subscriber: Subscriber) {
    if (globalSubscriber != null) {
        throw IllegalStateException("Global default subscriber has already been set")
    }
    globalSubscriber = subscriber
}

/**
 * Gets the current global [Subscriber], if one has been set.
 */
fun globalDefault(): Subscriber? = globalSubscriber

/**
 * Executes [block] with the given [subscriber] temporarily active.
 */
fun <T> withDefault(subscriber: Subscriber, block: () -> T): T {
    val previous = globalSubscriber
    globalSubscriber = subscriber
    return try {
        block()
    } finally {
        globalSubscriber = previous
    }
}

/**
 * Marker interface for sealed traits.
 */
interface Sealed<T>
