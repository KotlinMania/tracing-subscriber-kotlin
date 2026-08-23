package io.github.kotlinmania.tracingsubscriber.registry

import kotlin.reflect.KClass
import kotlin.reflect.safeCast

/**
 * Type-safe heterogeneous map for storing per-span extensions.
 */
class Extensions {
    private val map = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> get(key: KClass<T>): T? = key.safeCast(map[key])

    inline fun <reified T : Any> get(): T? = get(T::class)

    fun <T : Any> insert(key: KClass<T>, value: T): T? {
        val previous = map.put(key, value)
        return key.safeCast(previous)
    }

    inline fun <reified T : Any> insert(value: T): T? = insert(T::class, value)

    fun <T : Any> remove(key: KClass<T>): T? {
        val removed = map.remove(key)
        return key.safeCast(removed)
    }

    inline fun <reified T : Any> remove(): T? = remove(T::class)

    fun <T : Any> contains(key: KClass<T>): Boolean = map.containsKey(key)

    inline fun <reified T : Any> contains(): Boolean = contains(T::class)

    fun clear() {
        map.clear()
    }

    val size: Int get() = map.size
    val isEmpty: Boolean get() = map.isEmpty()
}
