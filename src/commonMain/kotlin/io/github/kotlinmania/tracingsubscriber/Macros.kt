// port-lint: source macros.rs
package io.github.kotlinmania.tracingsubscriber.macros

/**
 * Convenience helper for inline lock acquisition or returning a default value.
 */
inline fun <T, R> tryLock(lock: () -> T?, onFailed: () -> R, block: (T) -> R): R {
    val l = lock() ?: return onFailed()
    return block(l)
}
