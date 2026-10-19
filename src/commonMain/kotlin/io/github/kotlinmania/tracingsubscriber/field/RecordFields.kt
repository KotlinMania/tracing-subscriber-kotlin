package io.github.kotlinmania.tracingsubscriber.field

/**
 * Visitor interface for recording key-value fields.
 */
interface Visit {
    fun recordF64(name: String, value: Double) {}

    fun recordI64(name: String, value: Long) {}

    fun recordU64(name: String, value: ULong) {
        recordI64(name, value.toLong())
    }

    fun recordBool(name: String, value: Boolean) {}

    fun recordStr(name: String, value: String) {}

    fun recordDebug(name: String, value: Any?) {}
}

/**
 * Extension trait implemented by types which can be recorded by a visitor.
 */
interface RecordFields {
    fun record(visitor: Visit)
}

/**
 * Marker for sealed RecordFields trait implementations.
 */
class RecordFieldsMarker
