// port-lint: tests registry/extensions.rs
// port-lint: tests registry/sharded.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracing.Attributes
import io.github.kotlinmania.tracing.FieldSet
import io.github.kotlinmania.tracing.Level
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracing.Record
import io.github.kotlinmania.tracing.ValueSet
import io.github.kotlinmania.tracingsubscriber.registry.Extensions
import io.github.kotlinmania.tracingsubscriber.registry.registry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RegistryTest {
    data class CustomContext(
        val name: String,
    )

    @Test
    fun testExtensionsHeterogeneousMap() {
        val ext = Extensions()
        assertTrue(ext.isEmpty)
        assertEquals(0, ext.size)

        ext.insert(CustomContext("trace-root"))
        ext.insert("extra-string")

        assertEquals(2, ext.size)
        assertTrue(ext.contains<CustomContext>())
        assertTrue(ext.contains<String>())
        assertFalse(ext.contains<Int>())

        assertEquals(CustomContext("trace-root"), ext.get<CustomContext>())
        assertEquals("extra-string", ext.get<String>())

        val removed = ext.remove<CustomContext>()
        assertEquals(CustomContext("trace-root"), removed)
        assertFalse(ext.contains<CustomContext>())
        assertEquals(1, ext.size)

        ext.clear()
        assertTrue(ext.isEmpty)
    }

    @Test
    fun testSpanLifecycleAndHierarchy() {
        val reg = registry()

        val rootMeta = Metadata("root_span", "target", Level.INFO)
        val rootId = reg.newSpan(Attributes(rootMeta, values = ValueSet(FieldSet(listOf("key1")), mapOf("key1" to "val1"))))

        assertNull(reg.currentSpan())
        reg.enter(rootId)
        assertEquals(rootId, reg.currentSpan())

        val childMeta = Metadata("child_span", "target", Level.DEBUG)
        val childId = reg.newSpan(Attributes(childMeta, isContextual = true, values = ValueSet(FieldSet(listOf("key2")), mapOf("key2" to "val2"))))

        reg.enter(childId)
        assertEquals(childId, reg.currentSpan())

        val childSpan = reg.span(childId)
        assertNotNull(childSpan)
        assertEquals("child_span", childSpan.name)
        assertEquals(rootId, childSpan.parent?.id)

        val scopeSpans = childSpan.scope().toList()
        assertEquals(2, scopeSpans.size)
        assertEquals("child_span", scopeSpans[0].name)
        assertEquals("root_span", scopeSpans[1].name)

        val rootToLeaf = childSpan.scope().fromRoot().toList()
        assertEquals(2, rootToLeaf.size)
        assertEquals("root_span", rootToLeaf[0].name)
        assertEquals("child_span", rootToLeaf[1].name)

        reg.record(childId, Record(ValueSet(FieldSet(listOf("key3")), mapOf("key3" to "val3"))))
        assertEquals("val2", childSpan.fields["key2"])
        assertEquals("val3", childSpan.fields["key3"])

        reg.exit(childId)
        assertEquals(rootId, reg.currentSpan())

        reg.exit(rootId)
        assertNull(reg.currentSpan())

        assertTrue(reg.tryClose(childId))
        assertNull(reg.span(childId))

        assertTrue(reg.tryClose(rootId))
        assertNull(reg.span(rootId))
    }
}
