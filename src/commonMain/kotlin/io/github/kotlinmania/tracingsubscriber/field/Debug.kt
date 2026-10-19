// port-lint: source field/debug.rs
package io.github.kotlinmania.tracingsubscriber.field

/**
 * A visitor wrapper that formats debug fields using alternate formatting.
 */
class Alt<T, V : MakeVisitor<T>>(
    val inner: V,
) : MakeVisitor<T> {
    companion object {
        fun <T, V : MakeVisitor<T>> new(inner: V): Alt<T, V> = Alt(inner)
    }

    override fun makeVisitor(target: T): Visit = AltVisitor(inner.makeVisitor(target))
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
