// port-lint: source fmt/time/chrono_crate.rs
package io.github.kotlinmania.tracingsubscriber.fmt.time

sealed class ChronoFmtType {
    object Rfc3339 : ChronoFmtType()

    data class Custom(
        val format: String,
    ) : ChronoFmtType()
}

class ChronoLocal(
    val format: ChronoFmtType = ChronoFmtType.Rfc3339,
) : FormatTime by DateTimeFormat() {
    companion object {
        fun rfc3339(): ChronoLocal = ChronoLocal(ChronoFmtType.Rfc3339)

        fun new(formatString: String): ChronoLocal = ChronoLocal(ChronoFmtType.Custom(formatString))
    }
}

class ChronoUtc(
    val format: ChronoFmtType = ChronoFmtType.Rfc3339,
) : FormatTime by DateTimeFormat() {
    companion object {
        fun rfc3339(): ChronoUtc = ChronoUtc(ChronoFmtType.Rfc3339)

        fun new(formatString: String): ChronoUtc = ChronoUtc(ChronoFmtType.Custom(formatString))
    }
}

fun chronoUtc(): ChronoUtc = ChronoUtc()

fun chronoLocal(): ChronoLocal = ChronoLocal()
