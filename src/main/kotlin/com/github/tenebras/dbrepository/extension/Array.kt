package com.github.tenebras.dbrepository.extension

import java.util.concurrent.ThreadLocalRandom

fun <T> Array<T>.random(): T = this[ThreadLocalRandom.current().nextInt(0, size)]

fun <T> Array<T>.toJson(entityExtension: Map<String, Any>, exclude: List<String> = emptyList()): String {
    return this.map { it.toMap(exclude, entityExtension) }.toJson()
}

fun <T> Array<T>.toJson(entityExtension: Any, exclude: List<String> = emptyList()): String {
    return this.toJson(entityExtension.toMap(), exclude)
}

fun <T> Array<T>.toJson(exclude: List<String> = emptyList()): String {
    return this.toJson(exclude = exclude)
}