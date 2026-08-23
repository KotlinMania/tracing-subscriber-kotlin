package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.fmt.CompactFields
import io.github.kotlinmania.tracingsubscriber.fmt.DefaultFields
import io.github.kotlinmania.tracingsubscriber.fmt.FmtLayer
import io.github.kotlinmania.tracingsubscriber.fmt.FullFormatEvent
import io.github.kotlinmania.tracingsubscriber.fmt.JsonFields
import io.github.kotlinmania.tracingsubscriber.layer.with
import io.github.kotlinmania.tracingsubscriber.registry.Registry
import io.github.kotlinmania.tracingsubscriber.registry.registry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FmtTest {
    @Test
    fun testDefaultFieldsFormatting() {
        val buffer = StringBuilder()
        DefaultFields.formatFields(buffer, mapOf("k1" to "v1", "k2" to 42))
        assertEquals("k1=v1 k2=42", buffer.toString())
    }

    @Test
    fun testCompactFieldsFormatting() {
        val buffer = StringBuilder()
        CompactFields.formatFields(buffer, mapOf("k1" to "v1", "k2" to 42))
        assertEquals("k1: v1, k2: 42", buffer.toString())
    }

    @Test
    fun testJsonFieldsFormatting() {
        val buffer = StringBuilder()
        JsonFields.formatFields(buffer, mapOf("name" to "alice", "count" to 3, "valid" to true))
        assertEquals("{\"name\":\"alice\",\"count\":3,\"valid\":true}", buffer.toString())
    }

    @Test
    fun testFmtSubscriberFormatting() {
        val logs = mutableListOf<String>()
        val reg = registry()
        val layer =
            FmtLayer<Registry>(
                formatEvent = FullFormatEvent(displayTimestamp = false, displayTarget = true, displayLevel = true),
                formatFields = DefaultFields,
                writer = { logs.add(it) },
            )
        val subscriber = reg.with(layer)

        val spanId =
            subscriber.newSpan(
                Attributes(
                    Metadata("my_span", "test_target", Level.INFO),
                    values = mapOf("span_key" to "span_val"),
                ),
            )
        subscriber.enter(spanId)

        val event =
            Event(
                Metadata("my_event", "test_target", Level.INFO),
                fields = mapOf("message" to "hello world"),
            )
        subscriber.event(event)

        assertEquals(1, logs.size)
        assertTrue(logs[0].contains("INFO"))
        assertTrue(logs[0].contains("my_span{span_key=span_val}"))
        assertTrue(logs[0].contains("test_target"))
        assertTrue(logs[0].contains("message=hello world"))
    }
}
