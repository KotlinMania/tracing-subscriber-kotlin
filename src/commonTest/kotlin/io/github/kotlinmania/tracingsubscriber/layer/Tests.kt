// port-lint: tests layer/tests.rs
package io.github.kotlinmania.tracingsubscriber.layer

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Interest
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.NoSubscriber
import io.github.kotlinmania.tracingsubscriber.core.Record
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NopLayer : Layer<Subscriber>

class StringSubscriber(
    val str: String,
) : Subscriber {
    override fun registerCallsite(metadata: Metadata): Interest = Interest.NEVER

    override fun enabled(metadata: Metadata): Boolean = false

    override fun newSpan(attributes: Attributes): SpanId = SpanId(1)

    override fun record(id: SpanId, values: Record) {}

    override fun recordFollowsFrom(span: SpanId, follows: SpanId) {}

    override fun event(event: Event) {}

    override fun enter(id: SpanId) {}

    override fun exit(id: SpanId) {}

    override fun cloneSpan(id: SpanId): SpanId = id

    override fun tryClose(id: SpanId): Boolean = true

    override fun currentSpan(): SpanId? = null
}

class LayerTests {
    @Test
    fun testLayerIsSubscriber() {
        val s = NopLayer().withSubscriber(NoSubscriber())
        assertNotNull(s)
    }

    @Test
    fun testTwoLayersAreSubscriber() {
        val s = NopLayer().andThen(NopLayer()).withSubscriber(NoSubscriber())
        assertNotNull(s)
    }

    @Test
    fun testThreeLayersAreSubscriber() {
        val s = NopLayer().andThen(NopLayer()).andThen(NopLayer()).withSubscriber(NoSubscriber())
        assertNotNull(s)
    }

    @Test
    fun testSubscribersHaveProperties() {
        val sub = StringSubscriber("subscriber")
        val s = NopLayer().withSubscriber(sub)
        assertEquals("subscriber", (s.inner as StringSubscriber).str)
    }
}
