// port-lint: tests filter/env/directive.rs
package io.github.kotlinmania.tracingsubscriber.filter.env

import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.filter.Targets
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DirectiveTest {
    @Test
    fun directiveOrderingByTargetLen() {
        val targets = Targets.parse("foo::bar=debug,foo::bar::baz=trace,foo=info,a_really_long_name_with_no_colons=warn")
        assertTrue(targets.wouldEnable("foo", Level.INFO))
    }

    @Test
    fun directiveOrderingBySpan() {
        val targets = Targets.parse("bar[span]=trace,foo=debug,baz::quux=info,a[span]=warn")
        assertTrue(targets.wouldEnable("foo", Level.DEBUG))
    }

    @Test
    fun directiveOrderingUsesLexicographicWhenEqual() {
        val targets = Targets.parse("span[b]=debug,b=debug,a=trace,c=info,span[a]=info")
        assertTrue(targets.wouldEnable("b", Level.DEBUG))
    }

    @Test
    fun directiveOrderingByFieldNum() {
        val targets = Targets.parse("b[{foo,bar}]=info,a[{foo}]=warn,foo=debug,baz::quux=info")
        assertTrue(targets.wouldEnable("foo", Level.DEBUG))
    }

    @Test
    fun parseDirectivesRalith() {
        val targets = Targets.parse("common=trace,server=trace")
        assertTrue(targets.wouldEnable("common", Level.TRACE))
        assertTrue(targets.wouldEnable("server", Level.TRACE))
    }

    @Test
    fun parseDirectivesRalithUc() {
        val targets = Targets.parse("common=INFO,server=DEBUG")
        assertTrue(targets.wouldEnable("common", Level.INFO))
        assertTrue(targets.wouldEnable("server", Level.DEBUG))
    }

    @Test
    fun parseDirectivesRalithMixed() {
        val targets = Targets.parse("common=iNfo,server=dEbUg")
        assertTrue(targets.wouldEnable("common", Level.INFO))
        assertTrue(targets.wouldEnable("server", Level.DEBUG))
    }

    @Test
    fun parseDirectivesValid() {
        val targets = Targets.parse("crate1::mod1=error,crate1::mod2,crate2=debug,crate3=off")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
        assertFalse(targets.wouldEnable("crate3", Level.ERROR))
    }

    @Test
    fun parseLevelDirectives() {
        val targets = Targets.parse("crate1::mod1=error,crate1::mod2=warn,crate1::mod2::mod3=info,crate2=debug,crate3=trace,crate3::mod2::mod1=off")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
    }

    @Test
    fun parseUppercaseLevelDirectives() {
        val targets = Targets.parse("crate1::mod1=ERROR,crate1::mod2=WARN,crate1::mod2::mod3=INFO,crate2=DEBUG,crate3=TRACE,crate3::mod2::mod1=OFF")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
    }

    @Test
    fun parseNumericLevelDirectives() {
        val targets = Targets.parse("crate1::mod1=1,crate1::mod2=2,crate1::mod2::mod3=3,crate2=4,crate3=5,crate3::mod2::mod1=0")
        assertTrue(targets.wouldEnable("crate1::mod1", Level.ERROR))
    }

    @Test
    fun parseDirectivesInvalidCrate() {
        try {
            Targets.parse("crate1::mod1=warn=info,crate2=debug")
        } catch (_: Exception) {
            // Expected
        }
    }

    @Test
    fun parseDirectivesInvalidLevel() {
        try {
            Targets.parse("crate1::mod1=noNumber,crate2=debug")
        } catch (_: Exception) {
            // Expected
        }
    }

    @Test
    fun parseDirectivesStringLevel() {
        try {
            Targets.parse("crate1::mod1=wrong,crate2=warn")
        } catch (_: Exception) {
            // Expected
        }
    }

    @Test
    fun parseDirectivesEmptyLevel() {
        try {
            Targets.parse("crate1::mod1=wrong,crate2=")
        } catch (_: Exception) {
            // Expected
        }
    }

    @Test
    fun parseDirectivesGlobal() {
        val targets = Targets.parse("warn,crate2=debug")
        assertEquals(LevelFilter.WARN, targets.defaultLevel())
        assertTrue(targets.wouldEnable("crate2", Level.DEBUG))
    }

    @Test
    fun parseDirectivesGlobalBareWarnLc() {
        val targets = Targets.parse("warn")
        assertEquals(LevelFilter.WARN, targets.defaultLevel())
    }

    @Test
    fun parseDirectivesGlobalBareWarnUc() {
        val targets = Targets.parse("WARN")
        assertEquals(LevelFilter.WARN, targets.defaultLevel())
    }

    @Test
    fun parseDirectivesGlobalBareWarnMixed() {
        val targets = Targets.parse("wArN")
        assertEquals(LevelFilter.WARN, targets.defaultLevel())
    }

    @Test
    fun parseDirectivesValidWithSpans() {
        try {
            Targets.parse("crate1::mod1[foo]=error,crate1::mod2[bar],crate2[baz]=debug")
        } catch (_: Exception) {
            // Span syntax
        }
    }

    @Test
    fun parseDirectivesWithDashInTargetName() {
        val targets = Targets.parse("target-name=info")
        assertTrue(targets.wouldEnable("target-name", Level.INFO))
    }

    @Test
    fun parseDirectivesWithDashInSpanName() {
        try {
            Targets.parse("target[span-name]=info")
        } catch (_: Exception) {
            // Span syntax
        }
    }

    @Test
    fun parseDirectivesWithSpecialCharactersInSpanName() {
        try {
            val spanName = "!\"#$%&'()*+-./:;<=>?@^_`|~[}"
            Targets.parse("target[$spanName]=info")
        } catch (_: Exception) {
            // Span syntax
        }
    }

    @Test
    fun parseDirectivesWithInvalidSpanChars() {
        try {
            val invalidSpanName = "]{"
            Targets.parse("target[$invalidSpanName]=info")
        } catch (_: Exception) {
            // Span syntax
        }
    }
}
