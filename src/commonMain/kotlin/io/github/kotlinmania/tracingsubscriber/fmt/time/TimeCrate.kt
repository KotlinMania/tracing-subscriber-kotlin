// port-lint: source fmt/time/time_crate.rs
package io.github.kotlinmania.tracingsubscriber.fmt.time

typealias UtcTime = DateTimeFormat
typealias LocalTime = DateTimeFormat

fun utcTime(): UtcTime = DateTimeFormat()

fun localTime(): LocalTime = DateTimeFormat()
