@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlin.time.Clock

/**
 * Default timestamp provider using wall clock.
 */
class SystemTime : FormatTime {
    override fun formatTime(writer: StringBuilder) {
        val now = Clock.System.now()
        writer.append(now.toString()).append(" ")
    }

    companion object {
        fun default(): SystemTime = SystemTime()
    }
}

fun time(): SystemTime = SystemTime()
