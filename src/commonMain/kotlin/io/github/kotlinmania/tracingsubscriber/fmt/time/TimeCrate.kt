// port-lint: source fmt/time/time_crate.rs
package io.github.kotlinmania.tracingsubscriber.fmt.time

class UtcTime : FormatTime by DateTimeFormat()

class LocalTime : FormatTime by DateTimeFormat()

class OffsetTime<F>(
    val format: F? = null,
) : FormatTime by DateTimeFormat()

fun utcTime(): UtcTime = UtcTime()

fun localTime(): LocalTime = LocalTime()

fun <F> offsetTime(format: F? = null): OffsetTime<F> = OffsetTime(format)
