// port-lint: source filter/directive.rs
package io.github.kotlinmania.tracingsubscriber.filter

import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata

/**
 * Indicates that a string could not be parsed as a filtering directive.
 */
class ParseError(
    message: String = "invalid filtering directive",
    cause: Throwable? = null,
) : Exception(message, cause)

/**
 * A directive which will statically enable or disable a given callsite.
 */
data class StaticDirective(
    val target: String? = null,
    val fieldNames: List<String> = emptyList(),
    val level: LevelFilter = LevelFilter.OFF,
) : Comparable<StaticDirective> {
    fun caresAbout(meta: Metadata): Boolean {
        if (target != null && !meta.target.startsWith(target)) {
            return false
        }
        return true
    }

    override fun compareTo(other: StaticDirective): Int {
        val targetLenA = target?.length ?: 0
        val targetLenB = other.target?.length ?: 0
        val targetCmp = targetLenB.compareTo(targetLenA)
        if (targetCmp != 0) return targetCmp
        return other.fieldNames.size.compareTo(fieldNames.size)
    }
}

/**
 * An ordered set of filtering directives.
 */
class DirectiveSet<T : Comparable<T>> {
    private val directives = mutableListOf<T>()
    var maxLevel: LevelFilter = LevelFilter.OFF
        private set

    val isEmpty: Boolean get() = directives.isEmpty()
    val size: Int get() = directives.size

    fun iter(): List<T> = directives.toList()

    fun add(directive: T, level: LevelFilter) {
        if (level.priority > maxLevel.priority) {
            maxLevel = level
        }
        directives.add(directive)
        directives.sort()
    }
}
