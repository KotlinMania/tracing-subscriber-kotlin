// port-lint: source tracing-subscriber/src/field/mod.rs
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
 * Creates new visitors.
 */
interface MakeVisitor<T> {
    fun makeVisitor(target: T): Visit
}

/**
 * A visitor that produces output once it has visited a set of fields.
 */
interface VisitOutput<Out> : Visit {
    fun finish(): Out

    fun visit(fields: RecordFields): Out {
        fields.record(this)
        return finish()
    }
}

/**
 * Extension trait implemented by types which can be recorded by a visitor.
 */
interface RecordFields {
    fun record(visitor: Visit)
}

/**
 * Extension trait implemented for all MakeVisitor implementations that produce a VisitOutput visitor.
 */
interface MakeOutput<T, Out> : MakeVisitor<T> {
    fun visitWith(target: T, fields: RecordFields): Out {
        val visitor = makeVisitor(target)
        if (visitor is VisitOutput<*>) {
            @Suppress("UNCHECKED_CAST")
            return (visitor as VisitOutput<Out>).visit(fields)
        }
        fields.record(visitor)
        @Suppress("UNCHECKED_CAST")
        return Unit as Out
    }
}

/**
 * Extension trait implemented by visitors that write formatted strings.
 */
interface VisitFmt : VisitOutput<String> {
    fun writer(): StringBuilder
}

/**
 * Extension trait providing MakeVisitor combinators.
 */
interface MakeExt<T> : MakeVisitor<T> {
    fun debugAlt(): Alt<MakeVisitor<T>> = Alt(this)

    fun displayMessages(): Messages<MakeVisitor<T>> = Messages(this)

    fun delimited(delimiter: String): Delimited<String, MakeVisitor<T>> = Delimited(delimiter, this)
}
