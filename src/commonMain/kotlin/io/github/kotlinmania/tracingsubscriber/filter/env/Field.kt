// port-lint: source tracing-subscriber/src/filter/env/field.rs
package io.github.kotlinmania.tracingsubscriber.filter.env

import io.github.kotlinmania.tracingsubscriber.core.LevelFilter

/**
 * Indicates that a field name was invalid.
 */
class BadName(
    message: String,
) : Exception(message)

/**
 * Match specification for a field.
 */
data class Match(
    val name: String,
    val value: ValueMatch? = null,
)

typealias FieldMatch = Match

data class MatchPattern(
    val pattern: Regex,
)

data class MatchDebug(
    val pattern: String,
)

class Matcher(
    var pattern: String,
) {
    fun writeStr(s: String): Boolean {
        if (s.length > pattern.length) return false
        if (pattern.startsWith(s)) {
            pattern = pattern.substring(s.length)
            return true
        }
        return false
    }
}

/**
 * Matched set of fields for a span.
 */
data class SpanMatch(
    val fields: Map<String, ValueMatch> = emptyMap(),
    val level: LevelFilter = LevelFilter.OFF,
    var hasMatched: Boolean = false,
)

class MatchVisitor(
    val inner: SpanMatch? = null,
)

/**
 * Field value match criterion.
 */
sealed class ValueMatch {
    data class Bool(
        val value: Boolean,
    ) : ValueMatch()

    data class I64(
        val value: Long,
    ) : ValueMatch()

    data class U64(
        val value: ULong,
    ) : ValueMatch()

    data class F64(
        val value: Double,
    ) : ValueMatch()

    data class Debug(
        val value: String,
    ) : ValueMatch()

    data class Pat(
        val pattern: Regex,
    ) : ValueMatch()

    fun matches(fieldVal: Any?): Boolean =
        when (this) {
            is Bool -> fieldVal == value
            is I64 -> (fieldVal as? Number)?.toLong() == value
            is U64 -> (fieldVal as? Number)?.toLong()?.toULong() == value
            is F64 -> (fieldVal as? Number)?.toDouble() == value
            is Debug -> fieldVal.toString() == value
            is Pat -> pattern.containsMatchIn(fieldVal.toString())
        }
}

/**
 * Matched set of fields for a callsite.
 */
data class CallsiteMatch(
    val fields: Map<String, ValueMatch> = emptyMap(),
    val level: LevelFilter = LevelFilter.OFF,
)
