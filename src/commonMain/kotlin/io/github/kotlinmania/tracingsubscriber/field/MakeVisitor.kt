package io.github.kotlinmania.tracingsubscriber.field

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
 * Extension trait implemented for all MakeVisitor implementations that produce a VisitOutput visitor.
 */
interface MakeOutput<T, Out> : MakeVisitor<T> {
    override fun makeVisitor(target: T): VisitOutput<Out>

    fun visitWith(target: T, fields: RecordFields): Out = makeVisitor(target).visit(fields)
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
    fun debugAlt(): Alt<T, MakeExt<T>> = Alt(this)

    fun displayMessages(): Messages<T, MakeExt<T>> = Messages(this)

    fun delimited(delimiter: String): Delimited<T, String, MakeExt<T>> = Delimited(delimiter, this)
}

/**
 * Marker for sealed MakeExt trait implementations.
 */
class MakeExtMarker<T>
