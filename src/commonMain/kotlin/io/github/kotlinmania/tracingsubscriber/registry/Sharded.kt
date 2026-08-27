// port-lint: source registry/sharded.rs
package io.github.kotlinmania.tracingsubscriber.registry

import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.filter.layerfilters.FilterMap

typealias ShardedRegistry = Registry
typealias Data = RegistrySpanData

class DataInner(
    val filterMap: FilterMap = FilterMap(),
    val parent: SpanId? = null,
    val extensions: Extensions = Extensions(),
)

class CloseGuard(
    val id: SpanId,
    val isClosing: Boolean = false,
)

class NullCallsite
