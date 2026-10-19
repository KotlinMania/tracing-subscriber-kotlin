// port-lint: tests fmt/writer.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracing.Level
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracingsubscriber.fmt.writer.MakeWriter
import io.github.kotlinmania.tracingsubscriber.fmt.writer.OptionalWriters
import io.github.kotlinmania.tracingsubscriber.fmt.writer.TestWriter
import io.github.kotlinmania.tracingsubscriber.fmt.writer.Writer
import io.github.kotlinmania.tracingsubscriber.fmt.writer.and
import io.github.kotlinmania.tracingsubscriber.fmt.writer.orElse
import io.github.kotlinmania.tracingsubscriber.fmt.writer.withFilter
import io.github.kotlinmania.tracingsubscriber.fmt.writer.withMaxLevel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WriterTest {
    class MockMakeWriter : MakeWriter {
        private val buffer = StringBuilder()

        override fun makeWriter(): Writer =
            Writer { str ->
                buffer.append(str)
            }

        fun output(): String = buffer.toString()
    }

    @Test
    fun testCustomWriterClosure() {
        val buffer = StringBuilder()
        val makeWriter = MakeWriter { Writer { buffer.append(it) } }
        val writer = makeWriter.makeWriter()
        writer.write("custom writer closure error")
        assertEquals("custom writer closure error", buffer.toString())
    }

    @Test
    fun testCustomWriterStruct() {
        val mock = MockMakeWriter()
        val writer = mock.makeWriter()
        writer.write("custom writer struct error")
        assertEquals("custom writer struct error", mock.output())
    }

    @Test
    fun testCombinatorsLevelFilters() {
        val infoMake = MockMakeWriter()
        val debugMake = MockMakeWriter()
        val warnMake = MockMakeWriter()
        val errorMake = MockMakeWriter()

        val combined =
            infoMake
                .withMaxLevel(Level.INFO)
                .and(debugMake.withMaxLevel(Level.DEBUG))
                .and(warnMake.withMaxLevel(Level.WARN))
                .and(errorMake.withMaxLevel(Level.ERROR))

        val levels = listOf(Level.TRACE, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR)
        for (lvl in levels) {
            val meta = Metadata("test", "target", lvl)
            val w = combined.makeWriterFor(meta)
            w.writeLine("${lvl.name} message")
        }

        val debugOutput = debugMake.output()
        val infoOutput = infoMake.output()
        val warnOutput = warnMake.output()
        val errorOutput = errorMake.output()

        // Level.DEBUG max means DEBUG, INFO, WARN, ERROR pass
        assertTrue(debugOutput.contains("DEBUG message"))
        assertTrue(debugOutput.contains("INFO message"))
        assertTrue(debugOutput.contains("WARN message"))
        assertTrue(debugOutput.contains("ERROR message"))

        // Level.INFO max means INFO, WARN, ERROR pass
        assertTrue(!infoOutput.contains("DEBUG message"))
        assertTrue(infoOutput.contains("INFO message"))
        assertTrue(infoOutput.contains("WARN message"))
        assertTrue(infoOutput.contains("ERROR message"))

        // Level.WARN max means WARN, ERROR pass
        assertTrue(!warnOutput.contains("INFO message"))
        assertTrue(warnOutput.contains("WARN message"))
        assertTrue(warnOutput.contains("ERROR message"))

        // Level.ERROR max means only ERROR passes
        assertTrue(!errorOutput.contains("WARN message"))
        assertTrue(errorOutput.contains("ERROR message"))
    }

    @Test
    fun testCombinatorsOrElse() {
        val first = MockMakeWriter()
        val fallback = MockMakeWriter()

        val filteredFirst = first.withFilter { it.level == Level.WARN }
        val combined = filteredFirst.orElse(fallback)

        val debugMeta = Metadata("test", "target", Level.DEBUG)
        val warnMeta = Metadata("test", "target", Level.WARN)

        // Debug fails first, falls back to fallback
        combined.makeWriterFor(debugMeta).write("fallback debug")
        // Warn passes first
        combined.makeWriterFor(warnMeta).write("first warn")

        assertEquals("first warn", first.output())
        assertEquals("fallback debug", fallback.output())
    }

    @Test
    fun testCombinatorsOrElseChain() {
        val errMake = MockMakeWriter()
        val warnMake = MockMakeWriter()
        val infoMake = MockMakeWriter()
        val debugMake = MockMakeWriter()

        val chain =
            errMake.withFilter { it.level == Level.ERROR }.orElse(
                warnMake.withFilter { it.level == Level.WARN }.orElse(
                    infoMake.withFilter { it.level == Level.INFO }.orElse(
                        debugMake.withFilter { it.level == Level.DEBUG },
                    ),
                ),
            )

        val levels = listOf(Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG)
        for (lvl in levels) {
            val meta = Metadata("test", "target", lvl)
            chain.makeWriterFor(meta).write("${lvl.name} ")
        }

        assertEquals("ERROR ", errMake.output())
        assertEquals("WARN ", warnMake.output())
        assertEquals("INFO ", infoMake.output())
        assertEquals("DEBUG ", debugMake.output())
    }

    @Test
    fun testCombinatorsAnd() {
        val a = MockMakeWriter()
        val b = MockMakeWriter()
        val combined = a.and(b)

        val meta = Metadata("test", "target", Level.INFO)
        val writer = combined.makeWriterFor(meta)
        writer.write("broadcast message")

        assertEquals("broadcast message", a.output())
        assertEquals("broadcast message", b.output())
    }

    @Test
    fun testOptionalWriters() {
        val noneWriter = OptionalWriters.none<TestWriter>()
        val someWriter = OptionalWriters.some(TestWriter())
        val fromNull = OptionalWriters.from<TestWriter>(null)
        val fromVal = OptionalWriters.from(TestWriter())

        // Writing to none does not throw and goes to sink
        noneWriter.write("discarded")
        fromNull.write("discarded")

        someWriter.write("kept")
        fromVal.write("kept too")
    }

    @Test
    fun testTestWriterWithStderr() {
        val writer = TestWriter.withStderr()
        writer.write("stderr test")
        assertEquals("stderr test", writer.output())
    }
}
