// port-lint: tests layer/tests.rs
package io.github.kotlinmania.tracingsubscriber.layer

import io.github.kotlinmania.tracing.Attributes
import io.github.kotlinmania.tracing.Event
import io.github.kotlinmania.tracing.Id
import io.github.kotlinmania.tracing.Interest
import io.github.kotlinmania.tracing.Metadata
import io.github.kotlinmania.tracing.NoSubscriber
import io.github.kotlinmania.tracing.Record
import io.github.kotlinmania.tracing.Subscriber
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NopLayer : Layer<Subscriber>

class StringSubscriber(
    val str: String,
) : Subscriber {
    override fun registerCallsite(metadata: Metadata): Interest = Interest.NEVER

    override fun enabled(metadata: Metadata): Boolean = false

    override fun newSpan(attributes: Attributes): Id = Id(1)

    override fun record(id: Id, values: Record) {}

    override fun recordFollowsFrom(span: Id, follows: Id) {}

    override fun event(event: Event) {}

    override fun enter(id: Id) {}

    override fun exit(id: Id) {}

    override fun cloneSpan(id: Id): Id = id

    override fun tryClose(id: Id): Boolean = true

    override fun currentSpan(): Id? = null
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
