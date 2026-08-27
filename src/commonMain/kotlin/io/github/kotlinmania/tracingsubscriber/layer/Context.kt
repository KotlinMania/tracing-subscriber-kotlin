// port-lint: source tracing-subscriber/src/layer/context.rs
package io.github.kotlinmania.tracingsubscriber.layer

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.registry.LookupSpan
import io.github.kotlinmania.tracingsubscriber.registry.Scope
import io.github.kotlinmania.tracingsubscriber.registry.SpanRef

/**
 * Context provided to a [Layer] to interact with the underlying [Subscriber].
 */
class Context<out S : Subscriber>(
    val subscriber: S?,
    val filterId: Long = 0L,
) {
    fun currentSpan(): SpanId? = subscriber?.currentSpan()

    fun enabled(metadata: Metadata): Boolean = subscriber?.enabled(metadata) ?: true

    fun event(event: Event) {
        subscriber?.event(event)
    }

    fun lookupSpan(id: SpanId): SpanRef<LookupSpan>? = (subscriber as? LookupSpan)?.span(id)

    fun lookupCurrent(): SpanRef<LookupSpan>? = currentSpan()?.let { lookupSpan(it) }

    fun eventSpan(event: Event): SpanRef<LookupSpan>? =
        when {
            event.isRoot -> null
            event.parent != null -> lookupSpan(event.parent)
            event.isContextual -> lookupCurrent()
            else -> null
        }

    fun eventScope(event: Event): Scope<LookupSpan>? = eventSpan(event)?.scope()

    fun metadata(id: SpanId): Metadata? = lookupSpan(id)?.metadata

    fun exists(id: SpanId): Boolean = lookupSpan(id) != null

    fun spanScope(id: SpanId): Scope<LookupSpan>? = lookupSpan(id)?.scope()
}
