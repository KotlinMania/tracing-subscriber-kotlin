// port-lint: source fmt/writer.rs
package io.github.kotlinmania.tracingsubscriber.fmt.writer

import io.github.kotlinmania.tracingsubscriber.core.Metadata

/**
 * Interface for writing formatted log output.
 */
fun interface Writer {
    fun write(str: String)

    fun writeLine(str: String) {
        write(str + "\n")
    }
}

/**
 * Factory for creating [Writer] instances.
 */
fun interface MakeWriter {
    fun makeWriter(): Writer

    fun makeWriterFor(metadata: Metadata): Writer = makeWriter()
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
 * Extension trait providing combinators for [MakeWriter].
 */
interface MakeWriterExt : MakeWriter {
    fun orElse(other: MakeWriter): MakeWriter =
        MakeWriter {
            val w = makeWriter()
            w
        }
}
