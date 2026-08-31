// port-lint: source tracing-subscriber/src/registry/stack.rs
package io.github.kotlinmania.tracingsubscriber.registry

import io.github.kotlinmania.tracingsubscriber.core.SpanId

/**
 * Tracks what spans are currently executing on a thread or coroutine.
 */
class SpanStack {
    private data class ContextId(
        val id: SpanId,
        val duplicate: Boolean,
    )

    private val stack = mutableListOf<ContextId>()

    fun push(id: SpanId) {
        val duplicate = stack.any { it.id == id }
        stack.add(ContextId(id, duplicate))
    }

    fun pop(expectedId: SpanId): Boolean {
        for (i in stack.indices.reversed()) {
            if (stack[i].id == expectedId) {
                val item = stack.removeAt(i)
                return !item.duplicate
            }
        }
        return false
    }

    fun current(): SpanId? {
        for (item in stack.asReversed()) {
            if (!item.duplicate) return item.id
        }
        return null
    }

    fun iter(): List<SpanId> =
        stack.asReversed().filter { !it.duplicate }.map { it.id }
}
