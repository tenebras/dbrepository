package com.github.tenebras.dbrepository.extension

import java.util.concurrent.ThreadLocalRandom

fun <T> Array<T>.random(): T = this[ThreadLocalRandom.current().nextInt(0, size)]

fun <T> Array<T>.toJson(exclude: List<String> = emptyList(), append: (T) -> Map<String, Any?>): String {
    return this.map { it.toMap(exclude) + append.invoke(it) }.toJson()
}

fun <T> Array<T>.toJson(exclude: List<String> = emptyList()): String {
    return this.toJson(emptyMap(), exclude)
}

fun <T> Iterable<T>.toJson(exclude: List<String> = emptyList(), append: (T) -> Map<String, Any?>): String {
    return this.map { it.toMap(exclude) + append.invoke(it) }.toJson()
}