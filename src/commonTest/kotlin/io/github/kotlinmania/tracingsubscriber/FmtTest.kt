// port-lint: tests fmt/fmt_layer.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracing.Attributes
import io.github.kotlinmania.tracing.Event
import io.github.kotlinmania.tracing.FieldSet
import io.github.kotlinmania.tracing.Level
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracing.ValueSet
import io.github.kotlinmania.tracingsubscriber.fmt.format.JsonFormat
import io.github.kotlinmania.tracingsubscriber.fmt.format.format
import io.github.kotlinmania.tracingsubscriber.fmt.writer.MakeWriter
import io.github.kotlinmania.tracingsubscriber.fmt.writer.Writer
import io.github.kotlinmania.tracingsubscriber.layer.with
import io.github.kotlinmania.tracingsubscriber.registry.Registry
import io.github.kotlinmania.tracingsubscriber.registry.registry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FmtTest {
    @Test
    fun testFormatFullEvent() {
        val buffer = StringBuilder()
        val writer =
            object : Writer {
                override fun write(str: String) {
                    buffer.append(str)
                }

                override fun writeLine(str: String) {
                    buffer.append(str).append("\n")
                }
            }
        val fmt = format()
        val event =
            Event(
                Metadata("test_event", "my_target", Level.INFO),
                values = ValueSet(FieldSet(listOf("message", "key")), mapOf("message" to "hello world", "key" to "value")),
            )
        fmt.formatEvent(event, writer)
        val output = buffer.toString()
        assertTrue(output.contains("INFO"))
        assertTrue(output.contains("my_target"))
        assertTrue(output.contains("hello world"))
        assertTrue(output.contains("key=value"))
    }

    @Test
    fun testFormatJsonEvent() {
        val buffer = StringBuilder()
        val writer =
            object : Writer {
                override fun write(str: String) {
                    buffer.append(str)
                }

                override fun writeLine(str: String) {
                    buffer.append(str).append("\n")
                }
            }
        val jsonFmt = JsonFormat()
        val event =
            Event(
                Metadata("test_event", "json_target", Level.DEBUG),
                values = ValueSet(FieldSet(listOf("key1")), mapOf("key1" to "val1")),
            )
        jsonFmt.formatEvent(event, writer)
        val output = buffer.toString()
        assertTrue(output.contains("\"level\":\"DEBUG\""))
        assertTrue(output.contains("\"target\":\"json_target\""))
        assertTrue(output.contains("\"key1\":\"val1\""))
    }

    @Test
    fun testFmtSubscriberFormatting() {
        val logs = mutableListOf<String>()
        val reg = registry()
        val fmtLayer =
            io.github.kotlinmania.tracingsubscriber.fmt
                .Layer<Registry>()
                .withWriter(
                    MakeWriter {
                        object : Writer {
                            override fun write(str: String) {
                                logs.add(str)
                            }

                            override fun writeLine(str: String) {
                                logs.add(str)
                            }
                        }
                    },
                )
        val subscriber = reg.with(fmtLayer)

        val spanId =
            subscriber.newSpan(
                Attributes(
                    Metadata("my_span", "test_target", Level.INFO),
                    values = ValueSet(FieldSet(listOf("span_key")), mapOf("span_key" to "span_val")),
                ),
            )
        subscriber.enter(spanId)

        val event =
            Event(
                Metadata("my_event", "test_target", Level.INFO),
                values = ValueSet(FieldSet(listOf("message")), mapOf("message" to "hello world")),
            )
        subscriber.event(event)

        assertEquals(1, logs.size)
        assertTrue(logs[0].contains("INFO"))
        assertTrue(logs[0].contains("test_target"))
        assertTrue(logs[0].contains("hello world"))
    }
}
