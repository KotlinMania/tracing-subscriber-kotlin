// port-lint: source filter/targets.rs
package io.github.kotlinmania.tracingsubscriber.filter

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer

/**
 * Filter that enables or disables spans and events based on their target and level.
 */
class Targets :
    Filter<Subscriber>,
    Layer<Subscriber>,
    Iterable<Pair<String, LevelFilter>> {
    private val targetDirectives = mutableMapOf<String, LevelFilter>()
    private var defaultLevelFilter: LevelFilter? = null

    fun withTarget(target: String, level: LevelFilter): Targets {
        targetDirectives[target] = level
        return this
    }

    fun withTarget(target: String, level: Level): Targets =
        withTarget(target, LevelFilter.fromLevel(level))

    fun withTargets(targets: Iterable<Pair<String, LevelFilter>>): Targets {
        for ((target, level) in targets) {
            withTarget(target, level)
        }
        return this
    }

    fun withDefault(level: LevelFilter): Targets {
        defaultLevelFilter = level
        return this
    }

    fun withDefault(level: Level): Targets = withDefault(LevelFilter.fromLevel(level))

    fun defaultLevel(): LevelFilter? = defaultLevelFilter

    fun wouldEnable(target: String, level: Level): Boolean {
        val levelFilter = findLevelForTarget(target) ?: (defaultLevelFilter ?: LevelFilter.OFF)
        return levelFilter.contains(level)
    }

    private fun findLevelForTarget(target: String): LevelFilter? {
        // Exact match first
        targetDirectives[target]?.let { return it }

        // Find longest prefix match
        var bestMatch: String? = null
        var bestLength = -1

        for (prefix in targetDirectives.keys) {
            if (target.startsWith(prefix) && prefix.length > bestLength) {
                // If prefix is exact or followed by '::' or '.'
                if (target.length == prefix.length ||
                    target.startsWith("$prefix::") ||
                    target.startsWith("$prefix.") ||
                    target.startsWith("$prefix/")
                ) {
                    bestMatch = prefix
                    bestLength = prefix.length
                }
            }
        }

        return bestMatch?.let { targetDirectives[it] }
    }

    override fun enabled(metadata: Metadata, context: Context<Subscriber>): Boolean =
        wouldEnable(metadata.target, metadata.level)

    override fun eventEnabled(event: Event, context: Context<Subscriber>): Boolean =
        enabled(event.metadata, context)

    override fun callsiteEnabled(metadata: Metadata): Interest =
        if (wouldEnable(metadata.target, metadata.level)) Interest.ALWAYS else Interest.NEVER

    override fun registerCallsite(metadata: Metadata): Interest = callsiteEnabled(metadata)

    override fun maxLevelHint(): LevelFilter? {
        var max = defaultLevelFilter ?: LevelFilter.OFF
        for (filter in targetDirectives.values) {
            if (filter.priority > max.priority) {
                max = filter
            }
        }
        return max
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Targets) return false
        return targetDirectives == other.targetDirectives && defaultLevelFilter == other.defaultLevelFilter
    }

    override fun hashCode(): Int = 31 * targetDirectives.hashCode() + (defaultLevelFilter?.hashCode() ?: 0)

    override fun toString(): String {
        val list = mutableListOf<String>()
        defaultLevelFilter?.let { list.add(it.name.lowercase()) }
        for ((target, level) in targetDirectives) {
            list.add("$target=${level.name.lowercase()}")
        }
        return list.joinToString(",")
    }

    override fun iterator(): Iterator<Pair<String, LevelFilter>> =
        targetDirectives.entries.map { it.key to it.value }.iterator()

    fun iter(): Iter = Iter(iterator())

    fun intoIter(): IntoIter = IntoIter(iterator())

    companion object {
        fun new(): Targets = Targets()

        fun parse(str: String): Targets {
            val targets = Targets()
            val trimmed = str.trim()
            if (trimmed.isEmpty()) return targets

            for (part in trimmed.split(',')) {
                val partTrimmed = part.trim()
                if (partTrimmed.isEmpty()) continue

                if (partTrimmed.contains('=')) {
                    val split = partTrimmed.split('=', limit = 2)
                    val target = split[0].trim()
                    val levelStr = split[1].trim()
                    val level = LevelFilter.fromString(levelStr)
                    targets.withTarget(target, level)
                } else {
                    // Could be a bare level or a target without level (defaults to TRACE in upstream)
                    val levelOrNull = LevelFilter.fromStringOrNull(partTrimmed)
                    if (levelOrNull != null) {
                        targets.withDefault(levelOrNull)
                    } else {
                        targets.withTarget(partTrimmed, LevelFilter.TRACE)
                    }
                }
            }
            return targets
        }
    }
}

/**
 * An owning iterator over the target-level pairs of a [Targets] filter.
 */
class IntoIter internal constructor(
    private val iterator: Iterator<Pair<String, LevelFilter>>,
) : Iterator<Pair<String, LevelFilter>> by iterator

/**
 * An iterator over the target-level pairs of a [Targets] filter.
 */
class Iter internal constructor(
    private val iterator: Iterator<Pair<String, LevelFilter>>,
) : Iterator<Pair<String, LevelFilter>> by iterator
