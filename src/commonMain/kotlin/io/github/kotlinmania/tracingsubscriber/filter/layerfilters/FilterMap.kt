package io.github.kotlinmania.tracingsubscriber.filter.layerfilters

import io.github.kotlinmania.tracing.Interest

/**
 * Uniquely identifies an individual filter instance in the context of a subscriber.
 */
data class FilterId(
    val id: Long,
)

/**
 * Bitset mapping for per-layer filter results.
 */
data class FilterMap(
    var bits: ULong = 0uL,
) {
    fun isEnabled(id: FilterId): Boolean = (bits and (1uL shl id.id.toInt())) == 0uL

    fun disable(id: FilterId) {
        bits = bits or (1uL shl id.id.toInt())
    }

    fun enable(id: FilterId) {
        bits = bits and (1uL shl id.id.toInt()).inv()
    }
}

class FilterState(
    var enabled: FilterMap = FilterMap(),
    var interest: Interest? = null,
    val counters: DebugCounters = DebugCounters(),
)

class DebugCounters(
    var inFilterPass: Int = 0,
    var inCallsitePass: Int = 0,
)

class MagicPlfDowncastMarker(
    val id: FilterId,
)

class FmtBitset(
    val bits: ULong,
)
