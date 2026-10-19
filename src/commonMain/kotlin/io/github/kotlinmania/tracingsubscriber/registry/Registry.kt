package io.github.kotlinmania.tracingsubscriber.registry

import io.github.kotlinmania.tracing.Attributes
import io.github.kotlinmania.tracing.Event
import io.github.kotlinmania.tracing.Id
import io.github.kotlinmania.tracing.Interest
import io.github.kotlinmania.tracing.LevelFilter
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracing.Record
import io.github.kotlinmania.tracing.Subscriber

class RegistrySpanData(
    override val id: Id,
    override val metadata: Metadata,
    override val parent: Id?,
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
    private val spans = mutableMapOf<Id, RegistrySpanData>()
    private val spanStack = mutableListOf<Id>()

    override fun registerCallsite(metadata: Metadata): Interest = Interest.ALWAYS

    override fun enabled(metadata: Metadata): Boolean = true

    override fun newSpan(attributes: Attributes): Id {
        val id = Id(nextSpanIdValue++)
        val span =
            RegistrySpanData(
                id = id,
                metadata = attributes.metadata,
                parent = attributes.parent ?: currentSpan(),
            ).apply {
                recordFields(attributes.values.values)
            }
        spans[id] = span
        return id
    }

    override fun record(id: Id, values: Record) {
        val span = spans[id] ?: return
        span.recordFields(values.values.values)
    }

    override fun recordFollowsFrom(span: Id, follows: Id) {}

    override fun event(event: Event) {}

    override fun enter(id: Id) {
        spanStack.add(id)
    }

    override fun exit(id: Id) {
        val lastIndex = spanStack.lastIndexOf(id)
        if (lastIndex >= 0) {
            spanStack.removeAt(lastIndex)
        }
    }

    override fun currentSpan(): Id? = spanStack.lastOrNull()

    override fun cloneSpan(id: Id): Id {
        val span = spans[id] ?: return id
        span.refCount++
        return id
    }

    override fun tryClose(id: Id): Boolean {
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

    override fun spanData(id: Id): SpanData? = spans[id]

    override fun span(id: Id): SpanRef<LookupSpan>? {
        val data = spans[id] ?: return null
        return SpanRef(id, data, this)
    }

    override fun registerFilter(): Long = nextFilterIdValue++

    fun activeSpans(): List<Id> = spanStack.toList()

    companion object {
        fun default(): Registry = Registry()
    }
}

/**
 * Returns a new [Registry].
 */
fun registry(): Registry = Registry()
