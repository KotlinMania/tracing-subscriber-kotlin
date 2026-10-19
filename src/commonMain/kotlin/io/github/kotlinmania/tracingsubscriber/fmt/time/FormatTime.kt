package io.github.kotlinmania.tracingsubscriber.fmt.time

/**
 * A type that can measure and format the current time.
 */
interface FormatTime {
    fun formatTime(writer: StringBuilder)

    companion object {
        inline operator fun invoke(crossinline block: (StringBuilder) -> Unit): FormatTime =
            object : FormatTime {
                override fun formatTime(writer: StringBuilder) = block(writer)
            }
    }
}
