// port-lint: source filter/env/builder.rs
package io.github.kotlinmania.tracingsubscriber.filter.env

import io.github.kotlinmania.tracingsubscriber.core.LevelFilter

/**
 * A builder for constructing new [EnvFilter]s.
 */
class Builder(
    var regex: Boolean = true,
    var env: String? = null,
    var defaultDirective: Directive? = null,
) {
    fun withRegex(regex: Boolean): Builder {
        this.regex = regex
        return this
    }

    fun withEnv(env: String): Builder {
        this.env = env
        return this
    }

    fun withDefaultDirective(directive: Directive): Builder {
        this.defaultDirective = directive
        return this
    }

    fun withDefaultDirective(level: LevelFilter): Builder {
        this.defaultDirective = Directive(level = level)
        return this
    }

    fun parse(spec: String): EnvFilter {
        val filter = EnvFilter(builder = this)
        if (spec.isBlank()) {
            defaultDirective?.let { filter.addDirective(it) }
        } else {
            val directives = spec.split(',')
            for (d in directives) {
                val trimmed = d.trim()
                if (trimmed.isNotEmpty()) {
                    filter.addDirective(Directive.parse(trimmed))
                }
            }
        }
        return filter
    }

    fun parseLossy(spec: String): EnvFilter =
        try {
            parse(spec)
        } catch (_: Exception) {
            val filter = EnvFilter(builder = this)
            defaultDirective?.let { filter.addDirective(it) }
            filter
        }
}
