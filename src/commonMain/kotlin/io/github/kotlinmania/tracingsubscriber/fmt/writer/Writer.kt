// port-lint: source tracing-subscriber/src/fmt/writer.rs
package io.github.kotlinmania.tracingsubscriber.fmt.writer

import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.Metadata

/**
 * Interface for writing formatted log output.
 */
interface Writer {
    fun write(str: String)

    fun writeLine(str: String) {
        write(str + "\n")
    }

    companion object {
        inline operator fun invoke(crossinline block: (String) -> Unit): Writer =
            object : Writer {
                override fun write(str: String) = block(str)
            }
    }
}

/**
 * Factory for creating [Writer] instances.
 */
interface MakeWriter {
    fun makeWriter(): Writer

    fun makeWriterFor(metadata: Metadata): Writer = makeWriter()

    companion object {
        inline operator fun invoke(crossinline block: () -> Writer): MakeWriter =
            object : MakeWriter {
                override fun makeWriter(): Writer = block()
            }
    }
}

/**
 * Standard out writer.
 */
class StdoutWriter : Writer {
    override fun write(str: String) {
        print(str)
    }
}

/**
 * In-memory test writer.
 */
class TestWriter(
    private val onWrite: (String) -> Unit = {},
) : Writer {
    private val buffer = StringBuilder()

    override fun write(str: String) {
        buffer.append(str)
        onWrite(str)
    }

    fun output(): String = buffer.toString()
}

/**
 * A writer that is one of two types implementing [Writer].
 */
sealed class EitherWriter<out A : Writer, out B : Writer> : Writer {
    data class A<out A : Writer>(
        val writer: A,
    ) : EitherWriter<A, Nothing>() {
        override fun write(str: String) = writer.write(str)
    }

    data class B<out B : Writer>(
        val writer: B,
    ) : EitherWriter<Nothing, B>() {
        override fun write(str: String) = writer.write(str)
    }
}

/**
 * No-op sink writer.
 */
class SinkWriter : Writer {
    override fun write(str: String) {}
}

typealias OptionalWriter<T> = EitherWriter<T, SinkWriter>

/**
 * A [MakeWriter] combinator for max level filter.
 */
class WithMaxLevel<M : MakeWriter>(
    val make: M,
    val level: Level,
) : MakeWriter {
    override fun makeWriter(): Writer = make.makeWriter()

    override fun makeWriterFor(metadata: Metadata): Writer =
        if (metadata.level <= level) make.makeWriterFor(metadata) else SinkWriter()
}

/**
 * A [MakeWriter] combinator for min level filter.
 */
class WithMinLevel<M : MakeWriter>(
    val make: M,
    val level: Level,
) : MakeWriter {
    override fun makeWriter(): Writer = make.makeWriter()

    override fun makeWriterFor(metadata: Metadata): Writer =
        if (metadata.level >= level) make.makeWriterFor(metadata) else SinkWriter()
}

/**
 * A [MakeWriter] combinator with a predicate filter.
 */
class WithFilter<M : MakeWriter>(
    val make: M,
    val filter: (Metadata) -> Boolean,
) : MakeWriter {
    override fun makeWriter(): Writer = make.makeWriter()

    override fun makeWriterFor(metadata: Metadata): Writer =
        if (filter(metadata)) make.makeWriterFor(metadata) else SinkWriter()
}

/**
 * Combines two [MakeWriter]s with fallback.
 */
class OrElse<A : MakeWriter, B : MakeWriter>(
    val inner: A,
    val orElse: B,
) : MakeWriter {
    override fun makeWriter(): Writer = inner.makeWriter()

    override fun makeWriterFor(metadata: Metadata): Writer {
        val w = inner.makeWriterFor(metadata)
        return if (w !is SinkWriter) w else orElse.makeWriterFor(metadata)
    }
}

/**
 * Combines two [MakeWriter]s writing to both.
 */
class Tee<A : MakeWriter, B : MakeWriter>(
    val a: A,
    val b: B,
) : MakeWriter {
    override fun makeWriter(): Writer =
        Writer { str ->
            a.makeWriter().write(str)
            b.makeWriter().write(str)
        }

    override fun makeWriterFor(metadata: Metadata): Writer =
        Writer { str ->
            a.makeWriterFor(metadata).write(str)
            b.makeWriterFor(metadata).write(str)
        }
}

class MutexGuardWriter<W : Writer>(
    val inner: W,
) : Writer by inner

class ArcWriter<W : Writer>(
    val inner: W,
) : Writer by inner

class WriteAdaptor(
    val writer: Writer,
) : Writer by writer

class BoxMakeWriter(
    val inner: MakeWriter,
    val name: String = "BoxMakeWriter",
) : MakeWriter by inner

class Boxed(
    val inner: Writer,
) : Writer by inner

/**
 * Extension trait providing combinators for [MakeWriter].
 */
interface MakeWriterExt : MakeWriter {
    fun withMaxLevel(level: Level): WithMaxLevel<MakeWriterExt> = WithMaxLevel(this, level)

    fun withMinLevel(level: Level): WithMinLevel<MakeWriterExt> = WithMinLevel(this, level)

    fun withFilter(filter: (Metadata) -> Boolean): WithFilter<MakeWriterExt> = WithFilter(this, filter)

    fun <B : MakeWriter> orElse(other: B): OrElse<MakeWriterExt, B> = OrElse(this, other)

    fun <B : MakeWriter> and(other: B): Tee<MakeWriterExt, B> = Tee(this, other)
}
