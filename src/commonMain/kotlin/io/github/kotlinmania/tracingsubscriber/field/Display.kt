// port-lint: source field/display.rs
package io.github.kotlinmania.tracingsubscriber.field

/**
 * A visitor wrapper that ensures any strings named "message" are formatted using Display semantics.
 */
class Messages<T, V : MakeVisitor<T>>(
    val inner: V,
) : MakeVisitor<T> {
    companion object {
        fun <T, V : MakeVisitor<T>> new(inner: V): Messages<T, V> = Messages(inner)
    }

    override fun makeVisitor(target: T): Visit = MessageVisitor(inner.makeVisitor(target))
}

/**
 * Visitor wrapper that records "message" fields via Display formatting.
 */
class MessageVisitor(
    val inner: Visit,
) : Visit {
    override fun recordF64(name: String, value: Double) {
        inner.recordF64(name, value)
    }

    override fun recordI64(name: String, value: Long) {
        inner.recordI64(name, value)
    }

    override fun recordU64(name: String, value: ULong) {
        inner.recordU64(name, value)
    }

    override fun recordBool(name: String, value: Boolean) {
        inner.recordBool(name, value)
    }

    override fun recordStr(name: String, value: String) {
        if (name == "message") {
            inner.recordDebug(name, value)
        } else {
            inner.recordStr(name, value)
        }
    }

    override fun recordDebug(name: String, value: Any?) {
        inner.recordDebug(name, value)
    }
}
