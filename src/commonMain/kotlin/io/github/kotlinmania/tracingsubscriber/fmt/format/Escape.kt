// port-lint: source tracing-subscriber/src/fmt/format/escape.rs
package io.github.kotlinmania.tracingsubscriber.fmt.format

/**
 * Escapes ANSI control sequences to sanitize output strings.
 */
fun escape(str: String): String {
    val sb = StringBuilder()
    for (ch in str) {
        when (ch) {
            '\u001b' -> sb.append("\\x1b")
            '\u0007' -> sb.append("\\x07")
            '\u0008' -> sb.append("\\x08")
            '\u000c' -> sb.append("\\x0c")
            '\u007f' -> sb.append("\\x7f")
            in '\u0080'..'\u009f' -> sb.append("\\u{${ch.code.toString(16)}}")
            else -> sb.append(ch)
        }
    }
    return sb.toString()
}

/**
 * A writer that wraps another writer and escapes control sequences.
 */
class EscapingWriter<W>(
    val inner: W,
)
