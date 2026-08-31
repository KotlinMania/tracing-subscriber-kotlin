// port-lint: tests tracing-subscriber/src/filter/level.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LevelTest {
    @Test
    fun testLevelOrdering() {
        assertTrue(Level.ERROR < Level.WARN)
        assertTrue(Level.WARN < Level.INFO)
        assertTrue(Level.INFO < Level.DEBUG)
        assertTrue(Level.DEBUG < Level.TRACE)
    }

    @Test
    fun testLevelFromString() {
        assertEquals(Level.TRACE, Level.fromString("trace"))
        assertEquals(Level.DEBUG, Level.fromString("DEBUG"))
        assertEquals(Level.INFO, Level.fromString("Info"))
        assertEquals(Level.WARN, Level.fromString("warn"))
        assertEquals(Level.WARN, Level.fromString("warning"))
        assertEquals(Level.ERROR, Level.fromString("error"))
        assertNull(Level.fromString("unknown"))
    }

    @Test
    fun testLevelFilterContains() {
        val filter = LevelFilter.INFO
        assertTrue(filter.contains(Level.ERROR))
        assertTrue(filter.contains(Level.WARN))
        assertTrue(filter.contains(Level.INFO))
        assertFalse(filter.contains(Level.DEBUG))
        assertFalse(filter.contains(Level.TRACE))

        val off = LevelFilter.OFF
        assertFalse(off.contains(Level.ERROR))
        assertFalse(off.contains(Level.TRACE))

        val trace = LevelFilter.TRACE
        assertTrue(trace.contains(Level.ERROR))
        assertTrue(trace.contains(Level.TRACE))
    }

    @Test
    fun testLevelFilterParsing() {
        assertEquals(LevelFilter.OFF, LevelFilter.fromString("off"))
        assertEquals(LevelFilter.ERROR, LevelFilter.fromString("error"))
        assertEquals(LevelFilter.ERROR, LevelFilter.fromString("1"))
        assertEquals(LevelFilter.WARN, LevelFilter.fromString("warn"))
        assertEquals(LevelFilter.WARN, LevelFilter.fromString("warning"))
        assertEquals(LevelFilter.WARN, LevelFilter.fromString("2"))
        assertEquals(LevelFilter.INFO, LevelFilter.fromString("info"))
        assertEquals(LevelFilter.INFO, LevelFilter.fromString("3"))
        assertEquals(LevelFilter.DEBUG, LevelFilter.fromString("debug"))
        assertEquals(LevelFilter.DEBUG, LevelFilter.fromString("4"))
        assertEquals(LevelFilter.TRACE, LevelFilter.fromString("trace"))
        assertEquals(LevelFilter.TRACE, LevelFilter.fromString("5"))
    }

    @Test
    fun testLevelFilterToLevel() {
        assertNull(LevelFilter.OFF.toLevel())
        assertEquals(Level.ERROR, LevelFilter.ERROR.toLevel())
        assertEquals(Level.WARN, LevelFilter.WARN.toLevel())
        assertEquals(Level.INFO, LevelFilter.INFO.toLevel())
        assertEquals(Level.DEBUG, LevelFilter.DEBUG.toLevel())
        assertEquals(Level.TRACE, LevelFilter.TRACE.toLevel())
    }
}
