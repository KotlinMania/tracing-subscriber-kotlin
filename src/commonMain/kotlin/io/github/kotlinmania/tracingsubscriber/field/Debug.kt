// port-lint: source tracing-subscriber/src/field/debug.rs
package io.github.kotlinmania.tracingsubscriber.field

/**
 * A visitor wrapper that formats debug fields using alternate formatting.
 */
class Alt<V : MakeVisitor<*>>(
    val inner: V,
) : MakeVisitor<Any?> {
    companion object {
        fun <V : MakeVisitor<*>> new(inner: V): Alt<V> = Alt(inner)
    }

    override fun makeVisitor(target: Any?): Visit {
        @Suppress("UNCHECKED_CAST")
        val v = (inner as MakeVisitor<Any?>).makeVisitor(target)
        return AltVisitor(v)
    }
}

/**
 * Visitor wrapper that delegates debug formatting.
 */
class AltVisitor(
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
        inner.recordStr(name, value)
    }

    override fun recordDebug(name: String, value: Any?) {
        inner.recordDebug(name, value)
    }
}
