// port-lint: source fmt/time/time_crate.rs
package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlinx.datetime.TimeZone

typealias UtcTime = DateTimeFormat
typealias LocalTime = DateTimeFormat

fun utcTime(): UtcTime = DateTimeFormat(TimeZone.UTC)

fun localTime(): LocalTime = DateTimeFormat(TimeZone.currentSystemDefault())
