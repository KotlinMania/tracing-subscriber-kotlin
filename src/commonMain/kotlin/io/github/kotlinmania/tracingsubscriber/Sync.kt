// port-lint: source sync.rs
package io.github.kotlinmania.tracingsubscriber

/**
 * Reentrant read/write lock abstraction for Kotlin Multiplatform.
 */
class RwLock<T>(
    private var value: T,
) {
    fun <R> read(block: (T) -> R): R = block(value)

    fun <R> write(block: (T) -> R): R = block(value)

    fun getMut(): T = value

    fun set(newValue: T) {
        value = newValue
    }
}
