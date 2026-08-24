// port-lint: source filter/env/mod.rs
package io.github.kotlinmania.tracingsubscriber.filter.env

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.Filter
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer

/**
 * A [Layer] which filters spans and events based on a set of filter directives.
 */
class EnvFilter(
    val builder: Builder = Builder(),
) : Filter<Subscriber>,
    Layer<Subscriber> {
    private val directives = mutableListOf<Directive>()
    private var maxLevel: LevelFilter = LevelFilter.OFF

    companion object {
        fun builder(): Builder = Builder()

        fun new(spec: String): EnvFilter = builder().parse(spec)

        fun fromDefaultEnv(): EnvFilter = builder().parse("")
    }

    fun addDirective(directive: Directive): EnvFilter {
        if (directive.level.priority > maxLevel.priority) {
            maxLevel = directive.level
        }
        directives.add(directive)
        directives.sort()
        return this
    }

    override fun enabled(metadata: Metadata, context: Context<Subscriber>): Boolean {
        for (d in directives) {
            if (d.caresAbout(metadata)) {
                return d.level.contains(metadata.level)
            }
        }
        return false
    }

    override fun callsiteEnabled(metadata: Metadata): Interest =
        if (enabled(metadata, Context(null))) Interest.ALWAYS else Interest.NEVER

    override fun eventEnabled(event: Event, context: Context<Subscriber>): Boolean =
        enabled(event.metadata, context)

    override fun maxLevelHint(): LevelFilter? = maxLevel

    override fun toString(): String =
        directives.joinToString(",") {
            val target = it.target?.let { t -> "$t=" } ?: ""
            "$target${it.level.name.lowercase()}"
        }
}
