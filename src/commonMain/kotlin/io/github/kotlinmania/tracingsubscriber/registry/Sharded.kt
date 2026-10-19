// port-lint: source registry/sharded.rs
package io.github.kotlinmania.tracingsubscriber.registry

import io.github.kotlinmania.tracing.Id
import io.github.kotlinmania.tracingsubscriber.filter.layerfilters.FilterMap

typealias Data = RegistrySpanData

class DataInner(
    val filterMap: FilterMap = FilterMap(),
    val parent: Id? = null,
    val extensions: Extensions = Extensions(),
)

class CloseGuard(
    val id: Id,
    val isClosing: Boolean = false,
)

class NullCallsite
