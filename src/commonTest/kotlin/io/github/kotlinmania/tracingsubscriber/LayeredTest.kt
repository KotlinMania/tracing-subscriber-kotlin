// port-lint: tests tracing-subscriber/src/layer/layered.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.core.Attributes
import io.github.kotlinmania.tracingsubscriber.core.Event
import io.github.kotlinmania.tracingsubscriber.core.Level
import io.github.kotlinmania.tracingsubscriber.core.LevelFilter
import io.github.kotlinmania.tracingsubscriber.core.Metadata
import io.github.kotlinmania.tracingsubscriber.core.SpanId
import io.github.kotlinmania.tracingsubscriber.core.Subscriber
import io.github.kotlinmania.tracingsubscriber.filter.filterFn
import io.github.kotlinmania.tracingsubscriber.layer.Context
import io.github.kotlinmania.tracingsubscriber.layer.Layer
import io.github.kotlinmania.tracingsubscriber.layer.with
import io.github.kotlinmania.tracingsubscriber.registry.registry
import kotlin.test.Test
import kotlin.test.assertEquals

class LayeredTest {
    class RecordingLayer : Layer<Subscriber> {
        val events = mutableListOf<String>()
        val enteredSpans = mutableListOf<SpanId>()

        override fun onEvent(event: Event, context: Context<Subscriber>) {
            events.add(event.metadata.name)
        }

        override fun onEnter(id: SpanId, context: Context<Subscriber>) {
            enteredSpans.add(id)
        }
    }

    @Test
    fun testLayerCompositionAndFiltering() {
        val reg = registry()
        val layer1 = RecordingLayer()
        val layer2 = RecordingLayer()

        val filteredLayer2 = layer2.withFilter(filterFn { LevelFilter.WARN.contains(it.level) })
        val combined = layer1.andThen(filteredLayer2)
        val subscriber = reg.with(combined)

        val spanId = subscriber.newSpan(Attributes(Metadata("test_span", "test_target", Level.INFO)))
        subscriber.enter(spanId)

        assertEquals(1, layer1.enteredSpans.size)

        subscriber.event(Event(Metadata("info_event", "test_target", Level.INFO)))
        subscriber.event(Event(Metadata("warn_event", "test_target", Level.WARN)))

        assertEquals(listOf("info_event", "warn_event"), layer1.events)
        assertEquals(listOf("warn_event"), layer2.events)
    }
}
