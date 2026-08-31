// port-lint: tests tracing-subscriber/src/sync.rs
package io.github.kotlinmania.tracingsubscriber

import kotlin.test.Test
import kotlin.test.assertEquals

class SyncTest {
    @Test
    fun testRwLock() {
        val lock = RwLock("initial")
        val readVal = lock.read { it.uppercase() }
        assertEquals("INITIAL", readVal)

        lock.set("updated")
        val updatedVal = lock.read { it }
        assertEquals("updated", updatedVal)

        val writeVal = lock.write { "mutated" }
        assertEquals("mutated", writeVal)
        assertEquals("updated", lock.getMut())
    }
}
