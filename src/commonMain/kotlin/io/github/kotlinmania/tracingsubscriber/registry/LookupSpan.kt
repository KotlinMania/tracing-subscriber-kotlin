package io.github.kotlinmania.tracingsubscriber.registry

import io.github.kotlinmania.tracing.Id
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracing.Subscriber

/**
 * Access to stored data for an active span.
 */
interface SpanData {
    val id: Id
    val metadata: Metadata
    val parent: Id?
    val refCount: Int
    val extensions: Extensions
    val fields: Map<String, Any?>

    fun isEnabledFor(filterId: Long): Boolean
}

/**
 * Interface implemented by subscribers that support looking up span data by [Id].
 */
interface LookupSpan : Subscriber {
    fun spanData(id: Id): SpanData?

    fun span(id: Id): SpanRef<LookupSpan>?

    fun registerFilter(): Long
}

/**
 * Reference to an active span stored in a [LookupSpan] subscriber.
 */
class SpanRef<out S : LookupSpan>(
    val id: Id,
    val data: SpanData,
    val subscriber: S,
) {
    val metadata: Metadata get() = data.metadata
    val name: String get() = data.metadata.name
    val fields: Map<String, Any?> get() = data.fields
    val parent: SpanRef<LookupSpan>? get() = data.parent?.let { subscriber.span(it) }
    val parentId: Id? get() = data.parent
    val extensions: Extensions get() = data.extensions

    fun scope(): Scope<LookupSpan> =
        Scope(
            buildList {
                var current: Id? = id
                while (current != null) {
                    val span = subscriber.span(current) ?: break
                    add(span)
                    current = span.parentId
                }
            },
        )
}

/**
 * An iterator over the spans in the current scope, starting from the leaf and ending at the root.
 */
class Scope<out S : LookupSpan>(
    private val spans: List<SpanRef<S>>,
) : Iterable<SpanRef<S>> {
    override fun iterator(): Iterator<SpanRef<S>> = spans.iterator()

    fun fromRoot(): ScopeFromRoot<S> = ScopeFromRoot(spans.reversed())
}

/**
 * An iterator over spans in a scope from the root down to the leaf.
 */
class ScopeFromRoot<out S : LookupSpan>(
    private val spans: List<SpanRef<S>>,
) : Iterable<SpanRef<S>> {
    override fun iterator(): Iterator<SpanRef<S>> = spans.iterator()
}
