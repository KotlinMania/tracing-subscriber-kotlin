// port-lint: source fmt/time/chrono_crate.rs
package io.github.kotlinmania.tracingsubscriber.fmt.time

typealias ChronoUtc = DateTimeFormat

fun chronoUtc(): ChronoUtc = DateTimeFormat()

fun chronoLocal(): DateTimeFormat = DateTimeFormat()
