// port-lint: source tracing-subscriber/src/field/delimited.rs
package io.github.kotlinmania.tracingsubscriber.field

/**
 * A MakeVisitor wrapper that separates formatted fields with a delimiter.
 */
class Delimited<D : CharSequence, V : MakeVisitor<*>>(
    val delimiter: D,
    val inner: V,
) : MakeVisitor<Any?> {
    companion object {
        fun <D : CharSequence, V : MakeVisitor<*>> new(delimiter: D, inner: V): Delimited<D, V> =
            Delimited(delimiter, inner)
    }

    override fun makeVisitor(target: Any?): Visit {
        @Suppress("UNCHECKED_CAST")
        val v = (inner as MakeVisitor<Any?>).makeVisitor(target)
        return VisitDelimited(delimiter, v)
    }
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
