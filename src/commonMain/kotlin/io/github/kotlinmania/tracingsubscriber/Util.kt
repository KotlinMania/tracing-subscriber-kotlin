// port-lint: source util.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Subscriber

/**
 * Extension trait adding utility methods for subscriber initialization.
 */
interface SubscriberInitExt {
    fun tryInit(): Result<Unit> =
        try {
            if (this is Subscriber) {
                setGlobalDefault(this)
                Result.success(Unit)
            } else {
                Result.failure(TryInitError("Object is not a Subscriber"))
            }
        } catch (e: Exception) {
            Result.failure(TryInitError(e.message ?: "Failed to set global default subscriber", e))
        }

    fun init() {
        tryInit().getOrThrow()
    }
}

/**
 * Error returned by [SubscriberInitExt.tryInit] if a global default subscriber could not be initialized.
 */
class TryInitError(
    message: String = "failed to set global default subscriber",
    cause: Throwable? = null,
) : Exception(message, cause)
