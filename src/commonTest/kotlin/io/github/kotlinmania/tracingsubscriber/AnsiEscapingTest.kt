// port-lint: tests tests/ansi_escaping.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.fmt.format.Escape
import io.github.kotlinmania.tracingsubscriber.fmt.format.EscapingWriter
import io.github.kotlinmania.tracingsubscriber.fmt.format.JsonFormat
import io.github.kotlinmania.tracingsubscriber.fmt.format.escape
import io.github.kotlinmania.tracingsubscriber.fmt.writer.Writer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AnsiEscapingTest {
    class TestWriter : Writer {
        private val buffer = StringBuilder()

        override fun write(str: String) {
            buffer.append(str)
        }

        override fun writeLine(str: String) {
            buffer.append(str).append("\n")
        }

        fun getOutput(): String = buffer.toString()
    }

    @Test
    fun testEscapeFunction() {
        val raw = "\u001b]0;PWNED\u0007\u001b[2J\u0008\u000c\u007f"
        val sanitized = escape(raw)
        assertFalse(sanitized.contains('\u001b'), "Should not contain raw ESC")
        assertFalse(sanitized.contains('\u0007'), "Should not contain raw BEL")
        assertFalse(sanitized.contains('\u0008'), "Should not contain raw BS")
        assertFalse(sanitized.contains('\u000c'), "Should not contain raw FF")
        assertFalse(sanitized.contains('\u007f'), "Should not contain raw DEL")
        assertTrue(sanitized.contains("\\x1b"), "Should contain escaped \\x1b")
        assertTrue(sanitized.contains("\\x07"), "Should contain escaped \\x07")
        assertTrue(sanitized.contains("\\x08"), "Should contain escaped \\x08")
        assertTrue(sanitized.contains("\\x0c"), "Should contain escaped \\x0c")
        assertTrue(sanitized.contains("\\x7f"), "Should contain escaped \\x7f")
    }

    @Test
    fun testEscapeWrapper() {
        val malicious = "\u001b]0;PWNED\u0007"
        val wrapper = Escape(malicious)
        val output = wrapper.toString()
        assertFalse(output.contains('\u001b'), "Wrapper should not contain raw ESC")
        assertFalse(output.contains('\u0007'), "Wrapper should not contain raw BEL")
        assertTrue(output.contains("\\x1b"), "Wrapper should escape to \\x1b")
        assertTrue(output.contains("\\x07"), "Wrapper should escape to \\x07")
    }

    @Test
    fun testEscapingWriter() {
        val sb = StringBuilder()
        val writer = EscapingWriter(sb)
        writer.writeStr("\u001b[31mRed text\u001b[0m")
        val result = sb.toString()
        assertFalse(result.contains('\u001b'), "Writer output should not contain raw ESC")
        assertTrue(result.contains("\\x1b[31mRed text\\x1b[0m"))
    }

    @Test
    fun testMessageAnsiEscaping() {
        val maliciousInput = "\u001b]0;PWNED\u0007\u001b[2J\u0008\u000c\u007f"
        val sanitized = escape("User input: $maliciousInput")

        assertFalse(sanitized.contains('\u001b'), "Message output should not contain raw ESC characters")
        assertFalse(sanitized.contains('\u0007'), "Message output should not contain raw BEL characters")
        assertTrue(sanitized.contains("\\x1b"), "Message output should contain escaped \\x1b")
    }

    @Test
    fun testJsonAnsiEscaping() {
        val writer = TestWriter()
        val jsonFmt = JsonFormat()
        val maliciousInput = "\u001b]0;PWNED\u0007\u001b[2J"
        val sanitizedInput = escape(maliciousInput)

        val event =
            Event(
                Metadata("test_event", "test_target", Level.INFO),
                fields = mapOf("testing" to sanitizedInput, "user_input" to sanitizedInput),
            )
        jsonFmt.formatEvent(event, writer)
        val output = writer.getOutput()

        assertFalse(output.contains('\u001b'), "JSON output should not contain raw ESC characters")
        assertFalse(output.contains('\u0007'), "JSON output should not contain raw BEL characters")
    }

    @Test
    fun testAnsiSanitizationPreventsInjection() {
        val maliciousFieldValue = "\u001b]0;PWNED\u0007\u001b[2J"
        val sanitizedValue = escape(maliciousFieldValue)

        assertTrue(sanitizedValue.contains("\\x1b"), "Message content should be sanitized with \\x1b")
        assertFalse(sanitizedValue.contains("\u001b]0;PWNED"), "Message content should not contain raw ANSI sequences")
        assertFalse(sanitizedValue.contains('\u0007'), "Message content should not contain raw control characters")
    }

    @Test
    fun testC1ControlCharactersEscaping() {
        // C1 control characters: \u0080, \u0085, \u0090, \u009b, \u009c, \u009d, \u009e, \u009f
        val c1Controls = "\u0080\u0085\u0090\u009b\u009c\u009d\u009e\u009f"
        val sanitized = escape(c1Controls)

        assertFalse(sanitized.contains('\u0080'), "Output should not contain raw C1 control characters")
        assertFalse(sanitized.contains('\u009b'), "Output should not contain raw CSI character")
        assertFalse(sanitized.contains('\u009c'), "Output should not contain raw ST character")
        assertTrue(
            sanitized.contains("\\u{80}") || sanitized.contains("\\u{8"),
            "Should contain escaped C1 characters",
        )
    }
}
