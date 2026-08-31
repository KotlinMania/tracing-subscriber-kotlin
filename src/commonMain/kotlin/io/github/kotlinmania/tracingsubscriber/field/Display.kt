// port-lint: source tracing-subscriber/src/field/display.rs
package io.github.kotlinmania.tracingsubscriber.field

/**
 * A visitor wrapper that ensures any strings named "message" are formatted using Display semantics.
 */
class Messages<V : MakeVisitor<*>>(
    val inner: V,
) : MakeVisitor<Any?> {
    override fun makeVisitor(target: Any?): Visit {
        @Suppress("UNCHECKED_CAST")
        val v = (inner as MakeVisitor<Any?>).makeVisitor(target)
        return MessageVisitor(v)
    }
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
