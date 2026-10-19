// port-lint: source registry/stack.rs
package io.github.kotlinmania.tracingsubscriber.registry

import io.github.kotlinmania.tracing.Id

/**
 * Tracks what spans are currently executing on a thread or coroutine.
 */
class SpanStack {
    private data class ContextId(
        val id: Id,
        val duplicate: Boolean,
    )

    private val stack = mutableListOf<ContextId>()

    fun push(id: Id) {
        val duplicate = stack.any { it.id == id }
        stack.add(ContextId(id, duplicate))
    }

    fun pop(expectedId: Id): Boolean {
        for (i in stack.indices.reversed()) {
            if (stack[i].id == expectedId) {
                val item = stack.removeAt(i)
                return !item.duplicate
            }
        }
        return false
    }

    fun current(): Id? {
        for (item in stack.asReversed()) {
            if (!item.duplicate) return item.id
        }
        return null
    }

    fun iter(): List<Id> =
        stack.asReversed().filter { !it.duplicate }.map { it.id }
}
