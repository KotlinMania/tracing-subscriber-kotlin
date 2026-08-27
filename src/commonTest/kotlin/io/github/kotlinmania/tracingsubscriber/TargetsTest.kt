// port-lint: tests tracing-subscriber/src/filter/targets.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.filter.Targets
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TargetsTest {
    @Test
    fun testDirectivesParsing() {
        val targets = Targets.parse("crate1::mod1=error,crate1::mod2=warn,crate2=debug,crate3=off")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
        assertFalse(targets.wouldEnable("crate1::mod1", Level.WARN))
        assertTrue(targets.wouldEnable("crate1::mod2", Level.WARN))
        assertFalse(targets.wouldEnable("crate1::mod2", Level.INFO))
        assertTrue(targets.wouldEnable("crate2", Level.DEBUG))
        assertFalse(targets.wouldEnable("crate3", Level.ERROR))
    }

    @Test
    fun testTargetHierarchyMatching() {
        val targets =
            Targets()
                .withTarget("my_crate", Level.INFO)
                .withTarget("my_crate::interesting_module", Level.DEBUG)
                .withTarget("my_crate::annoying_module", LevelFilter.OFF)

        assertTrue(targets.wouldEnable("my_crate", Level.INFO))
        assertFalse(targets.wouldEnable("my_crate", Level.DEBUG))

        assertTrue(targets.wouldEnable("my_crate::interesting_module", Level.DEBUG))
        assertTrue(targets.wouldEnable("my_crate::interesting_module::sub", Level.DEBUG))
        assertFalse(targets.wouldEnable("my_crate::interesting_module", Level.TRACE))

        assertFalse(targets.wouldEnable("my_crate::annoying_module", Level.ERROR))
        assertFalse(targets.wouldEnable("other_crate", Level.ERROR))
    }

    @Test
    fun testDefaultLevel() {
        val targets =
            Targets()
                .withTarget("my_crate", Level.DEBUG)
                .withDefault(LevelFilter.WARN)

        assertEquals(LevelFilter.WARN, targets.defaultLevel())
        assertTrue(targets.wouldEnable("my_crate", Level.DEBUG))
        assertTrue(targets.wouldEnable("other_crate", Level.WARN))
        assertFalse(targets.wouldEnable("other_crate", Level.INFO))
    }

    @Test
    fun testParsingWithDefaultLevel() {
        val targets = Targets.parse("info,my_crate=debug")
        assertEquals(LevelFilter.INFO, targets.defaultLevel())
        assertTrue(targets.wouldEnable("my_crate", Level.DEBUG))
        assertTrue(targets.wouldEnable("other_crate", Level.INFO))
        assertFalse(targets.wouldEnable("other_crate", Level.DEBUG))
    }

    @Test
    fun testMaxLevelHint() {
        val targets =
            Targets()
                .withTarget("a", Level.INFO)
                .withTarget("b", Level.TRACE)
                .withDefault(LevelFilter.WARN)

        assertEquals(LevelFilter.TRACE, targets.maxLevelHint())
    }

    @Test
    fun parseLevelDirectives() {
        val targets = Targets.parse("crate1::mod1=error,crate1::mod2=warn,crate1::mod2::mod3=info,crate2=debug,crate3=trace,crate3::mod2::mod1=off")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
        assertTrue(targets.wouldEnable("crate1::mod2", Level.WARN))
        assertTrue(targets.wouldEnable("crate2", Level.DEBUG))
    }

    @Test
    fun parseUppercaseLevelDirectives() {
        val targets = Targets.parse("crate1::mod1=ERROR,crate1::mod2=WARN,crate1::mod2::mod3=INFO,crate2=DEBUG,crate3=TRACE,crate3::mod2::mod1=OFF")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
        assertTrue(targets.wouldEnable("crate1::mod2", Level.WARN))
    }

    @Test
    fun parseNumericLevelDirectives() {
        val targets = Targets.parse("crate1::mod1=1,crate1::mod2=2,crate1::mod2::mod3=3,crate2=4,crate3=5,crate3::mod2::mod1=0")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
    }

    @Test
    fun targetsIter() {
        val targets = Targets.parse("crate1::mod1=error,crate1::mod2,crate2=debug,crate3=off")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
    }

    @Test
    fun targetsIntoIter() {
        val targets = Targets.parse("crate1::mod1=error,crate1::mod2,crate2=debug,crate3=off")
        assertTrue(targets.wouldEnable("crate2", Level.DEBUG))
    }

    @Test
    fun targetsDefaultLevel() {
        val targets = Targets.parse("crate1::mod1=error,crate1::mod2,crate2=debug,crate3=off")
            .withDefault(LevelFilter.OFF)
        assertEquals(LevelFilter.OFF, targets.defaultLevel())
    }

    @Test
    fun sizeOfFilters() {
        val targets = Targets.parse("info")
        assertEquals(LevelFilter.INFO, targets.defaultLevel())
    }

    @Test
    fun displayRoundtrips() {
        val targets = Targets.parse("crate1=info,crate2=debug")
        assertTrue(targets.wouldEnable("crate1", Level.INFO))
    }
}
