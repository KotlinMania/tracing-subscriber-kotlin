// port-lint: source filter/layer_filters/combinator.rs
package io.github.kotlinmania.tracingsubscriber.filter.layerfilters

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.Filter
import io.github.kotlinmania.tracingsubscriber.layer.Context

/**
 * Combines two [Filter]s so that spans and events are enabled if and only if both filters return `true`.
 */
class And<A : Filter<S>, B : Filter<S>, S : Subscriber>(
    val a: A,
    val b: B,
) : Filter<S> {
    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        a.enabled(metadata, context) && b.enabled(metadata, context)

    override fun callsiteEnabled(metadata: Metadata): Interest {
        val interestA = a.callsiteEnabled(metadata)
        if (interestA.isNever()) return interestA
        val interestB = b.callsiteEnabled(metadata)
        if (!interestB.isAlways()) return interestB
        return interestA
    }

    override fun eventEnabled(event: Event, context: Context<S>): Boolean =
        a.eventEnabled(event, context) && b.eventEnabled(event, context)

    override fun maxLevelHint(): LevelFilter? {
        val hintA = a.maxLevelHint()
        val hintB = b.maxLevelHint()
        return when {
            hintA == null -> hintB
            hintB == null -> hintA
            hintA.priority <= hintB.priority -> hintA
            else -> hintB
        }
    }
}

/**
 * Combines two [Filter]s so that spans and events are enabled if either filter returns `true`.
 */
class Or<A : Filter<S>, B : Filter<S>, S : Subscriber>(
    val a: A,
    val b: B,
) : Filter<S> {
    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        a.enabled(metadata, context) || b.enabled(metadata, context)

    override fun callsiteEnabled(metadata: Metadata): Interest {
        val interestA = a.callsiteEnabled(metadata)
        if (interestA.isAlways()) return interestA
        val interestB = b.callsiteEnabled(metadata)
        if (interestB.isAlways()) return interestB
        if (interestA.isSometimes() || interestB.isSometimes()) return Interest.SOMETIMES
        return Interest.NEVER
    }

    override fun eventEnabled(event: Event, context: Context<S>): Boolean =
        a.eventEnabled(event, context) || b.eventEnabled(event, context)

    override fun maxLevelHint(): LevelFilter? {
        val hintA = a.maxLevelHint()
        val hintB = b.maxLevelHint()
        return when {
            hintA == null -> hintB
            hintB == null -> hintA
            hintA.priority >= hintB.priority -> hintA
            else -> hintB
        }
    }
}

/**
 * Inverts the result of a [Filter].
 */
class Not<A : Filter<S>, S : Subscriber>(
    val a: A,
) : Filter<S> {
    override fun enabled(metadata: Metadata, context: Context<S>): Boolean =
        !a.enabled(metadata, context)

    override fun callsiteEnabled(metadata: Metadata): Interest {
        val interest = a.callsiteEnabled(metadata)
        return when {
            interest.isAlways() -> Interest.NEVER
            interest.isNever() -> Interest.ALWAYS
            else -> Interest.SOMETIMES
        }
    }

    override fun eventEnabled(event: Event, context: Context<S>): Boolean =
        !a.eventEnabled(event, context)

    override fun maxLevelHint(): LevelFilter? = null
}
