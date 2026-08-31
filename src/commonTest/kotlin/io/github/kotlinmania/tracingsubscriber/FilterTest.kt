// port-lint: tests tracing-subscriber/src/filter/filter_fn.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.dynamicFilterFn
import io.github.kotlinmania.tracingsubscriber.filter.filterFn
import io.github.kotlinmania.tracingsubscriber.layer.Context
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FilterTest {
    @Test
    fun testFilterFn() {
        val filter = filterFn { metadata -> LevelFilter.INFO.contains(metadata.level) }
        val infoMeta = Metadata("test", "target", Level.INFO)
        val warnMeta = Metadata("test", "target", Level.WARN)
        val debugMeta = Metadata("test", "target", Level.DEBUG)

        val context = Context<Subscriber>(null)
        assertTrue(filter.enabled(infoMeta, context))
        assertTrue(filter.enabled(warnMeta, context))
        assertFalse(filter.enabled(debugMeta, context))
        assertEquals(Interest.ALWAYS, filter.callsiteEnabled(infoMeta))
    }

    @Test
    fun testDynamicFilterFn() {
        val filter = dynamicFilterFn<Subscriber> { metadata, _ -> metadata.target == "allowed" }
        val allowedMeta = Metadata("test", "allowed", Level.INFO)
        val blockedMeta = Metadata("test", "blocked", Level.INFO)

        val context = Context<Subscriber>(null)
        assertTrue(filter.enabled(allowedMeta, context))
        assertFalse(filter.enabled(blockedMeta, context))
    }
}
