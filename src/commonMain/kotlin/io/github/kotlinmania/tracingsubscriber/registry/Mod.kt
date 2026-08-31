// port-lint: source tracing-subscriber/src/registry/mod.rs
package io.github.kotlinmania.tracingsubscriber.registry

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Record
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber

/**
 * Access to stored data for an active span.
 */
interface SpanData {
    val id: SpanId
    val metadata: Metadata
    val parent: SpanId?
    val refCount: Int
    val extensions: Extensions
    val fields: Map<String, Any?>

    fun isEnabledFor(filterId: Long): Boolean
}

/**
 * Interface implemented by subscribers that support looking up span data by [SpanId].
 */
interface LookupSpan : Subscriber {
    fun spanData(id: SpanId): SpanData?

    fun span(id: SpanId): SpanRef<LookupSpan>?

    fun registerFilter(): Long
}

/**
 * Reference to an active span stored in a [LookupSpan] subscriber.
 */
class SpanRef<out S : LookupSpan>(
    val id: SpanId,
    val data: SpanData,
    val subscriber: S,
) {
    val metadata: Metadata get() = data.metadata
    val name: String get() = data.metadata.name
    val fields: Map<String, Any?> get() = data.fields
    val parent: SpanRef<LookupSpan>? get() = data.parent?.let { subscriber.span(it) }
    val parentId: SpanId? get() = data.parent
    val extensions: Extensions get() = data.extensions

    fun scope(): Scope<LookupSpan> =
        Scope(
            buildList {
                var current: SpanId? = id
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

class RegistrySpanData(
    override val id: SpanId,
    override val metadata: Metadata,
    override val parent: SpanId?,
    override var refCount: Int = 1,
    override val extensions: Extensions = Extensions(),
) : SpanData {
    private val storageFields: MutableMap<String, Any?> = mutableMapOf()
    override val fields: Map<String, Any?> get() = storageFields
    private val disabledFilters = mutableSetOf<Long>()

    fun recordFields(newFields: Map<String, Any?>) {
        storageFields.putAll(newFields)
    }

    fun disableFor(filterId: Long) {
        if (filterId != 0L) {
            disabledFilters.add(filterId)
        }
    }

    override fun isEnabledFor(filterId: Long): Boolean {
        if (filterId == 0L) return true
        return !disabledFilters.contains(filterId)
    }
}

/**
 * Canonical in-memory subscriber and span registry.
 */
class Registry :
    Subscriber,
    LookupSpan {
    private var nextSpanIdValue = 1L
    private var nextFilterIdValue = 1L
    private val spans = mutableMapOf<SpanId, RegistrySpanData>()
    private val spanStack = mutableListOf<SpanId>()

    override fun registerCallsite(metadata: Metadata): Interest = Interest.ALWAYS

    override fun enabled(metadata: Metadata): Boolean = true

    override fun newSpan(attributes: Attributes): SpanId {
        val id = SpanId(nextSpanIdValue++)
        val span =
            RegistrySpanData(
                id = id,
                metadata = attributes.metadata,
                parent = attributes.parent ?: currentSpan(),
            ).apply {
                recordFields(attributes.values)
            }
        spans[id] = span
        return id
    }

    override fun record(id: SpanId, values: Record) {
        val span = spans[id] ?: return
        span.recordFields(values.values)
    }

    override fun recordFollowsFrom(span: SpanId, follows: SpanId) {}

    override fun event(event: Event) {}

    override fun enter(id: SpanId) {
        spanStack.add(id)
    }

    override fun exit(id: SpanId) {
        val lastIndex = spanStack.lastIndexOf(id)
        if (lastIndex >= 0) {
            spanStack.removeAt(lastIndex)
        }
    }

    override fun currentSpan(): SpanId? = spanStack.lastOrNull()

    override fun cloneSpan(id: SpanId): SpanId {
        val span = spans[id] ?: return id
        span.refCount++
        return id
    }

    override fun tryClose(id: SpanId): Boolean {
        val span = spans[id] ?: return true
        span.refCount--
        if (span.refCount <= 0) {
            spans.remove(id)
            val index = spanStack.indexOf(id)
            if (index >= 0) {
                spanStack.removeAt(index)
            }
            return true
        }
        return false
    }

    override fun maxLevelHint(): LevelFilter? = null

    override fun spanData(id: SpanId): SpanData? = spans[id]

    override fun span(id: SpanId): SpanRef<LookupSpan>? {
        val data = spans[id] ?: return null
        return SpanRef(id, data, this)
    }

    override fun registerFilter(): Long = nextFilterIdValue++

    fun activeSpans(): List<SpanId> = spanStack.toList()
}

/**
 * Returns a new [Registry].
 */
fun registry(): Registry = Registry()
