// port-lint: source registry/extensions.rs
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

/**
 * A mutable view of a span's extensions.
 */
class ExtensionsMut(
    private val extensions: Extensions,
) {
    fun <T : Any> get(key: KClass<T>): T? = extensions.get(key)

    inline fun <reified T : Any> get(): T? = get(T::class)

    fun <T : Any> insert(key: KClass<T>, value: T) {
        val prev = extensions.insert(key, value)
        check(prev == null) { "Extension of type ${key.simpleName} was already present" }
    }

    inline fun <reified T : Any> insert(value: T) = insert(T::class, value)

    fun <T : Any> replace(key: KClass<T>, value: T): T? = extensions.insert(key, value)

    inline fun <reified T : Any> replace(value: T): T? = replace(T::class, value)

    fun <T : Any> remove(key: KClass<T>): T? = extensions.remove(key)

    inline fun <reified T : Any> remove(): T? = remove(T::class)
}

class ExtensionsInner(
    val extensions: Extensions = Extensions(),
)

class IdHasher
