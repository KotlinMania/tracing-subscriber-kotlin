// port-lint: source fmt/time/chrono_crate.rs
package io.github.kotlinmania.tracingsubscriber.fmt.time

import kotlinx.datetime.TimeZone

typealias ChronoUtc = DateTimeFormat

fun chronoUtc(): ChronoUtc = DateTimeFormat(TimeZone.UTC)

fun chronoLocal(): DateTimeFormat = DateTimeFormat(TimeZone.currentSystemDefault())
