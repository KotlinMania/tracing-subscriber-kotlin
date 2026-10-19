// port-lint: source field/delimited.rs
package io.github.kotlinmania.tracingsubscriber.field

/**
 * A MakeVisitor wrapper that separates formatted fields with a delimiter.
 */
class Delimited<T, D : CharSequence, V : MakeVisitor<T>>(
    val delimiter: D,
    val inner: V,
) : MakeVisitor<T> {
    companion object {
        fun <T, D : CharSequence, V : MakeVisitor<T>> new(delimiter: D, inner: V): Delimited<T, D, V> =
            Delimited(delimiter, inner)
    }

    override fun makeVisitor(target: T): Visit = VisitDelimited(delimiter, inner.makeVisitor(target))
}

/**
 * Visitor wrapper that inserts a delimiter after formatting each field value.
 */
class VisitDelimited<D : CharSequence, V : Visit>(
    val delimiter: D,
    val inner: V,
) : Visit {
    private var seen: Boolean = false

    private fun delimit() {
        if (seen && inner is VisitFmt) {
            inner.writer().append(delimiter)
        }
        seen = true
    }

    override fun recordF64(name: String, value: Double) {
        delimit()
        inner.recordF64(name, value)
    }

    override fun recordI64(name: String, value: Long) {
        delimit()
        inner.recordI64(name, value)
    }

    override fun recordU64(name: String, value: ULong) {
        delimit()
        inner.recordU64(name, value)
    }

    override fun recordBool(name: String, value: Boolean) {
        delimit()
        inner.recordBool(name, value)
    }

    override fun recordStr(name: String, value: String) {
        delimit()
        inner.recordStr(name, value)
    }

    override fun recordDebug(name: String, value: Any?) {
        delimit()
        inner.recordDebug(name, value)
    }
}
