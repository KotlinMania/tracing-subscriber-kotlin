// port-lint: source prelude.rs
package io.github.kotlinmania.tracingsubscriber

import io.github.kotlinmania.tracingsubscriber.field.MakeExt
import io.github.kotlinmania.tracingsubscriber.field.RecordFields
import io.github.kotlinmania.tracingsubscriber.fmt.writer.MakeWriterExt
import io.github.kotlinmania.tracingsubscriber.layer.Layer
import io.github.kotlinmania.tracingsubscriber.layer.SubscriberExt

typealias TracingSubscriberLayer<S> = Layer<S>
typealias TracingSubscriberSubscriberExt = SubscriberExt
typealias TracingSubscriberFieldMakeExt<T> = MakeExt<T>
typealias TracingSubscriberFieldRecordFields = RecordFields
typealias TracingSubscriberInitExt = SubscriberInitExt
typealias TracingSubscriberMakeWriterExt = MakeWriterExt
