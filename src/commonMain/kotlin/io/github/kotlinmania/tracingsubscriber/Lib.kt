package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Subscriber

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
