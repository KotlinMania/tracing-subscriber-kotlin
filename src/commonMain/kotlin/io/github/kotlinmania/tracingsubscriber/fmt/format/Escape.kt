// port-lint: source fmt/format/escape.rs
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
 * A wrapper that escapes ANSI sequences on-the-fly.
 * This avoids creating intermediate strings while providing security against terminal injection.
 */
class Escape<T>(
    val value: T,
) {
    fun fmt(appendable: Appendable) {
        val writer = EscapingWriter(appendable)
        writer.writeStr(value.toString())
    }

    override fun toString(): String {
        val sb = StringBuilder()
        fmt(sb)
        return sb.toString()
    }
}

/**
 * Helper class that escapes ANSI sequences as characters are written.
 */
class EscapingWriter<W : Appendable>(
    val inner: W,
) {
    fun writeStr(s: String): EscapingWriter<W> {
        for (ch in s) {
            writeChar(ch)
        }
        return this
    }

    fun writeChar(ch: Char): EscapingWriter<W> {
        when (ch) {
            '\u001b' -> inner.append("\\x1b")
            '\u0007' -> inner.append("\\x07")
            '\u0008' -> inner.append("\\x08")
            '\u000c' -> inner.append("\\x0c")
            '\u007f' -> inner.append("\\x7f")
            in '\u0080'..'\u009f' -> inner.append("\\u{${ch.code.toString(16)}}")
            else -> inner.append(ch)
        }
        return this
    }
}
