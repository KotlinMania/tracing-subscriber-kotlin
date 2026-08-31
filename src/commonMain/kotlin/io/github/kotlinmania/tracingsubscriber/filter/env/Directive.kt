// port-lint: source filter/env/directive.rs
package io.github.kotlinmania.tracingsubscriber.filter.env

import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata

/**
 * A single filtering directive for EnvFilter.
 */
data class Directive(
    val inSpan: String? = null,
    val fields: List<FieldMatch> = emptyList(),
    val target: String? = null,
    val level: LevelFilter = LevelFilter.OFF,
) : Comparable<Directive> {
    fun hasName(): Boolean = inSpan != null

    fun hasFields(): Boolean = fields.isNotEmpty()

    fun isDynamic(): Boolean = hasName() || hasFields()

    fun caresAbout(meta: Metadata): Boolean {
        if (target != null && !meta.target.startsWith(target)) {
            return false
        }
        return true
    }

    override fun compareTo(other: Directive): Int {
        val targetLenA = target?.length ?: 0
        val targetLenB = other.target?.length ?: 0
        val targetCmp = targetLenB.compareTo(targetLenA)
        if (targetCmp != 0) return targetCmp
        return other.fields.size.compareTo(fields.size)
    }

    companion object {
        fun parse(str: String): Directive {
            val trimmed = str.trim()
            if (trimmed.isEmpty()) return Directive(level = LevelFilter.OFF)

            if (trimmed.contains('=')) {
                val parts = trimmed.split('=', limit = 2)
                val targetPart = parts[0].trim()
                val levelPart = parts[1].trim()
                val level = LevelFilter.fromString(levelPart)
                return Directive(target = if (targetPart.isNotEmpty()) targetPart else null, level = level)
            }

            val levelOrNull = LevelFilter.fromStringOrNull(trimmed)
            if (levelOrNull != null) {
                return Directive(level = levelOrNull)
            }
            return Directive(target = trimmed, level = LevelFilter.TRACE)
        }
    }
}

/**
 * A set of matching field criteria.
 */
data class MatchSet<T>(
    val fieldMatches: List<T> = emptyList(),
    val baseLevel: LevelFilter = LevelFilter.OFF,
)

typealias CallsiteMatcher = MatchSet<CallsiteMatch>
typealias SpanMatcher = MatchSet<SpanMatch>

/**
 * State of directive parser.
 */
sealed class ParseState {
    object Start : ParseState()

    data class LevelOrTarget(
        val start: Int,
    ) : ParseState()

    data class Span(
        val spanStart: Int,
    ) : ParseState()

    data class Field(
        val fieldStart: Int,
    ) : ParseState()

    object Fields : ParseState()

    object Target : ParseState()

    data class Level(
        val levelStart: Int,
    ) : ParseState()

    object Complete : ParseState()
}
